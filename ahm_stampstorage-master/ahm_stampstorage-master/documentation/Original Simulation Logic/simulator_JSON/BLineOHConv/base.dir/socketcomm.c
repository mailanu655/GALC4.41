#include "platform.h"

#include <stdio.h>
#include <assert.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>

#ifdef MS_WINDOWS

#include <winsock2.h>
#include <windows.h>
#include "sys/timeb.h"
#define WINSOCK

#else /* MS_WINDOWS */

#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <netdb.h>
#endif /* MS_WINDOWS */

#ifdef WINSOCK

#define sockerrno           WSAGetLastError()
#define sockstrerror(e)		""
#define WOULDBLOCK          WSAEWOULDBLOCK
#define INPROGRESS          WSAEINPROGRESS
#define NOTCONN             WSAENOTCONN
#define BLOCKERROR			WSAEWOULDBLOCK

#else /* WINSOCK */

#define SOCKET_ERROR		-1
#define INVALID_SOCKET      -1
#define sockerrno           errno
#define sockstrerror(e)		strerror(e)
#define WOULDBLOCK          EWOULDBLOCK
#define INPROGRESS          EINPROGRESS
#define NOTCONN             ENOTCONN
#define BLOCKERROR			EINPROGRESS

#endif /* WINSOCK */

typedef struct Ptr2Socket {
	char*	name;
	char*	host;
	int		sock;
	int		flags;
	int		nonblocking;
	int		timeout;
	int		connected;
} Ptr2Socket, *mySocketptr;

static int connect_blocking = 0;
static int connect_timeout = 0;
static int socket_messages = 1;

typedef struct myPort {
	struct myPort* next;
	int port;
	int numaccepts;
	int socket;
} myPort;

static myPort* activePorts;
static myPort* freePorts;

static myPort* myPortNew(void);
static void myPortFree(myPort*);
static void bindport(int, int);
static void unbindport(int);
static int boundport(int);
static int validport(int);
static Ptr2Socket* mySocketNew(int, char*);
static int myOpenSocket(void);
static void myCloseSocket(int, int);
static void mySetSocketNonBlocking(int, int);
static Ptr2Socket* connect_port(char*, int);
static int myValidSocket(int);
int sock_SetMessages(int);
int sock_SetConnectBlocking(int);
int sock_SetConnectTimeOut(int);
Ptr2Socket* sock_ConnectService(char*, char*);
Ptr2Socket* sock_ConnectPort(char*, int);
int sock_CloseSocket(Ptr2Socket*);
int sock_IsValid(Ptr2Socket*);
int sock_Send(Ptr2Socket*, char*, int);
int sock_SendString(Ptr2Socket*, char*);
int sock_Read(Ptr2Socket*, char*, int);
char* sock_ReadString(Ptr2Socket*);
int sock_GetNonBlocking(Ptr2Socket*);
int sock_SetNonBlocking(Ptr2Socket*, int);
int sock_GetTimeOut(Ptr2Socket*);
int sock_SetTimeOut(Ptr2Socket*, int);
int sock_GetBufferSize(Ptr2Socket*);
int sock_SetBufferSize(Ptr2Socket*, int);
char* sock_GetName(Ptr2Socket*);
char* sock_GetHost(Ptr2Socket*);
int sock_GetNum(Ptr2Socket*);
char* sock_MachineName(void);

// Start of code

static myPort*
myPortNew()
{
	myPort* aPort = freePorts;
	
	if (aPort) {
		freePorts = freePorts->next;
	} else {
		aPort = (myPort*)xmalloc(sizeof(myPort));
	}
	aPort->port = -1;
	aPort->numaccepts = 1;
	aPort->socket = INVALID_SOCKET;
	aPort->next = activePorts;
	activePorts = aPort;
	return aPort;
}

static void
myPortFree(myPort* aPort)
{
	aPort->next = freePorts;
	freePorts = aPort;
}

static void
bindport(int port, int socket)
{
	myPort* aPort;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			aPort->socket = socket;
			return;
		}
	}
	aPort = myPortNew();
	aPort->port = port;
	aPort->numaccepts = 1;
	aPort->socket = socket;
}

static void
unbindport(int port)
{
	myPort* aPort;
	myPort* prev;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			if (prev) {
				prev->next = aPort->next;
			} else {
				activePorts = aPort->next;
			}
			myPortFree(aPort);
			return;
		}
		prev = aPort;
	}
}

static int
boundport(int port)
{
	myPort* aPort;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			return aPort->socket != INVALID_SOCKET ? aPort->socket : 0;
		}
	}
	return 0;
}

static int
validport(int port)
{
	myPort* aPort;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			return aPort->numaccepts != -1;
		}
	}
	return 0;
}

static Ptr2Socket*
mySocketNew(int sock, char* host)
{
	static char tmp[512];
	Ptr2Socket* theSocket = (Ptr2Socket*)xmalloc(sizeof(struct Ptr2Socket));
	
	sprintf(tmp, "Socket %d to %s", sock, host);
	theSocket->name = strdup(tmp);
	theSocket->host = strdup(host);
	theSocket->sock = sock;
	theSocket->flags = 1;
	theSocket->nonblocking = 0;
	theSocket->timeout = 0;
	theSocket->connected = 0;
	return theSocket;
}

static int
myOpenSocket(void)
{
	return socket(AF_INET, SOCK_STREAM, 0);
}

static void
myCloseSocket(int sock, int connected)
{
	if (sock == INVALID_SOCKET)
		return;

		/* Make the socket blocking */
	mySetSocketNonBlocking(sock, 0);
	
	if (connected) {
		shutdown(sock, 2);
	}
#ifdef WINSOCK
	if (closesocket(sock) == 0)
#else /* WINSOCK */
	if (close(sock) == 0)
#endif /* WINSOCK */
	{
		return;
	}

	switch(sockerrno) {
	case BLOCKERROR:
		message("Socket close would block");
		break;
	default:
		message("Socket close unknown error: errno %d", sockerrno);
		break;
	}
}

static void
mySetSocketNonBlocking(int sock, int flag)
{
	unsigned long nonblock = flag;
#ifndef WINSOCK
	int ret = ioctl(sock, FIONBIO, &nonblock);
#else /* WINSOCK */
	int ret = ioctlsocket(sock, FIONBIO, &nonblock);
#endif /* WINSOCK */

	if (ret < 0) {
		message("ioctl() error %d: %s.\n", sockerrno, sockstrerror(sockerrno));
	}
}

static Ptr2Socket*
connect_port(char *serverhost, int port)
{
	struct hostent *hp;
	struct sockaddr_in saddr;
	char connecthost[256] = "UNKNOWN";
	int ret;
	int sock;
	int nfound = 0;
	int numtimes = 0;
	int alreadyopened = FALSE;

	checknetstart();

	if (!connect_blocking && connect_timeout) {
		if (socket_messages) {
			message("Trying for %d seconds", connect_timeout);
		}
	}

	while (nfound <= 0) {
		if (alreadyopened == FALSE) {
			hp = gethostbyname(serverhost);

			if (hp == NULL) {
				message("Error: ConnectSocket could not find hostname %s",
					serverhost);
				return NULL;
			}

			memset((char *)&saddr, (int)0, sizeof(saddr));
			memcpy((char *)&saddr.sin_addr, hp->h_addr, hp->h_length);
			saddr.sin_family = hp->h_addrtype;
			saddr.sin_port = port;

			sock = myOpenSocket();
			
			if (sock == INVALID_SOCKET) {
				message("Error: ConnectSocket couldn't make socket");
				return NULL;
			}

			/* Make the socket Non-Blocking */
			mySetSocketNonBlocking(sock, 1);
			{
				int setret;
				struct linger lg;
				int on = 1;

				lg.l_onoff = 1;
				lg.l_linger = 5;

				setret = setsockopt(sock, SOL_SOCKET, SO_LINGER, (char*)&lg, sizeof(lg));
				if (setret != 0) {
					message("Error: ConnectSocket setsockopt error");
				}
				setret = setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, (char*)&on, sizeof(on));
				if (setret != 0) {
					message("Error: ConnectSocket setsockopt error");
				}
			}

			ret = connect(sock, (struct sockaddr*)&saddr, sizeof(saddr));

			if (ret >= 0) {
				nfound = 1;
				alreadyopened = TRUE;
			} else if (ret < 0 && (sockerrno == WOULDBLOCK || sockerrno == INPROGRESS)) {
				alreadyopened = TRUE;
			} else {
				message("Error: ConnectSocket connect error");
				myCloseSocket(sock, 0);
				return NULL;
			}
		} else {
			struct sockaddr_in getname;
			int getnamelen = sizeof(getname);
			struct hostent *hp;
			fd_set writefds;
			fd_set exceptfds;
			struct timeval tv;

			tv.tv_sec = 0;
			tv.tv_usec = 0;

			FD_ZERO(&writefds);
			FD_SET(sock, &writefds);

			FD_ZERO(&exceptfds);
			FD_SET(sock, &exceptfds);

#ifdef WINSOCK
			MilliSleep(100);
#endif /* WINSOCK */
			ret = select(sock + 1, 0, &writefds, &exceptfds, &tv);

			if (ret > 0) {
				if (FD_ISSET(sock, &writefds)) {
					nfound = 1;
				}
			} else if (ret == 0) {
				/* timeout */
				if (!connect_blocking) {
					numtimes++;
					if (numtimes > connect_timeout * 10) {
						if (socket_messages) {
							message("Warning: ConnectSocket timed out");
						}
						if (alreadyopened) {
							myCloseSocket(sock, 0);
						}
						alreadyopened = FALSE;
						sock = INVALID_SOCKET;
						nfound = 1;
						break;
					}
				}
#ifndef WINSOCK
				MilliSleep(100);
#endif /* WINSOCK */
				nfound = 0;
				continue;
			} else {
				message("Error: ConnectSocket select error %d", sockerrno);
				if (alreadyopened) {
					myCloseSocket(sock, 0);
				}
				alreadyopened = FALSE;
				return NULL;
			}


			/* Now check to make sure it is indeed up */
			ret = getpeername(sock, (struct sockaddr*)&getname, &getnamelen);

			if (ret < 0 && sockerrno == NOTCONN) {
				if (!connect_blocking) {
					numtimes++;
					if (numtimes > connect_timeout * 10) {
						if (socket_messages) {
							message("Warning: ConnectSocket timed out");
						}
						if (alreadyopened) {
							myCloseSocket(sock, 0);
						}
						alreadyopened = FALSE;
						sock = INVALID_SOCKET;
						nfound = 1;
						break;
					}
				}
				if (alreadyopened) {
					myCloseSocket(sock, 0);
				}
				alreadyopened = FALSE;
				sock = INVALID_SOCKET;
				MilliSleep(100);
				nfound = 0;
				continue;
			} else if (ret < 0) {
				message("Error: ConnectSocket getpeername error");
				if (alreadyopened) {
					myCloseSocket(sock, 0);
				}
				alreadyopened = FALSE;
				return NULL;
			}

			hp = gethostbyaddr((char*)&getname.sin_addr, sizeof(struct in_addr), getname.sin_family);
			if (hp == NULL) {
				message("Error: ConnectSocket gethostbyaddr error");
			} else {
				strcpy(connecthost, hp->h_name);
			}
		}
	}

	if (nfound > 0 && sock >= 0) {
		Ptr2Socket* aSocket = mySocketNew(sock, connecthost);

		aSocket->connected = 1;
		return aSocket;	
	} else {
		return NULL;
	}
}

int
sock_SetMessages(int on)
{
	socket_messages = on;
	return on;
}

int
sock_SetConnectBlocking(int flag)
{
	connect_blocking = flag;
	return 1;
}

int
sock_SetConnectTimeOut(int timeout)
{
	connect_timeout = timeout;
	return 1;
}

Ptr2Socket*
sock_ConnectService(char* serverhost, char* service)
{
	struct servent* sp = getservbyname(service, "tcp");

	if (sp == NULL) {
		message("Error: sock_ConnectService couldn't find service: %s, %s",
			service, "with tcp protocol");
		return NULL;
	}

	if (socket_messages) {
		message("%s to connect to '%s' with service '%s'",
			connect_blocking ? "Waiting" : "Trying", serverhost, service);
	}

	return connect_port(serverhost, sp->s_port);
}

Ptr2Socket*
sock_ConnectPort(char* serverhost, int port)
{
	short myport = (short)port;

	if (socket_messages) {
		message("%s to connect to '%s' on port: %d",
			connect_blocking ? "Waiting" : "Trying", serverhost, port);
	}

	port = (int)htons(myport);

	return connect_port(serverhost, port);
}

int
sock_CloseSocket(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		myCloseSocket(theSocket->sock, theSocket->connected);
		theSocket->connected = 0;
		if (socket_messages) {
			message("Closed socket %d", theSocket->sock );
		}
		theSocket->flags = 0;
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_CloseSocket %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

static int
myValidSocket(int sock)
{
	return sock != INVALID_SOCKET;
}

int
sock_IsValid(Ptr2Socket* theSocket)
{
	return theSocket && theSocket->sock != INVALID_SOCKET && theSocket->flags != 0 && theSocket->flags != -1;
}

int
sock_Send(Ptr2Socket* theSocket, char* msg, int length)
{
	if (sock_IsValid(theSocket)) {
		int total = 0;
		int sock = theSocket->sock;
		char* msgptr = msg;
		int ret;

		if (theSocket->nonblocking && theSocket->timeout) {
			struct sockaddr_in getname;
			int getnamelen = sizeof(getname);
			fd_set writefds;
			struct timeval tv;
	
			tv.tv_sec = 1;
			tv.tv_usec = 0;
	
			FD_ZERO(&writefds);
			FD_SET(sock, &writefds);
	
			ret = select(sock + 1, 0, &writefds, 0, &tv);
	
			if (ret > 0) {
				assert(FD_ISSET(sock, &writefds));
			} else {
				message("Error: sock_Send send error, closing socket");
				sock_CloseSocket(theSocket);
				return 0;
			}
	
			/* Now check to make sure it is indeed up */
			ret = getpeername(sock, (struct sockaddr*)&getname, &getnamelen);
	
			if (ret < 0) {
				message("Error: sock_Send send error, closing socket");
				sock_CloseSocket(theSocket);
				return 0;
			}
		}
	
		ret = send(sock, msg, length, 0);
	
		if (ret <= 0) {
			if (sockerrno != WOULDBLOCK) {
				message("Error: sock_Send send error, closing socket");
				sock_CloseSocket(theSocket);
			}
			return 0;
		} else {
			msgptr += ret;
			total += ret;
			length -= ret;
	
			while (length > 0) {
#if 0
				message("Continuing sending for %d of %d bytes", length, total);
#endif /* 0 */
				MilliSleep(10);
				ret = send(sock, msgptr, length, 0);
				if (ret <= 0) {
					if (sockerrno != WOULDBLOCK) {
						message("Error: sock_Send send error, closing socket");
						sock_CloseSocket(theSocket);
						return 0;
					}
					continue;
				}
				msgptr += ret;
				total += ret;
				length -= ret;
			}
		}
	
		return total;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				if (theSocket->flags == 0) {
					message("Error: sock_Send %d %s",
						theSocket->sock, "is not a valid socket or already closed");
				}
				theSocket->flags = -1;
			}
		}
		return 0;
	}
}

int
sock_SendString(Ptr2Socket* theSocket, char* value)
{
	static char* msg = NULL;
	short mylen;
	unsigned short len;
	int total = sizeof(len) + strlen(value);

	if (msg) {
		xfree(msg);
	}
	msg = xmalloc(total + 1);

	mylen = (short)strlen(value);
	len = htons(mylen);

	msg[0] = ((char*)&len)[0];
	msg[1] = ((char*)&len)[1];

	strcpy(msg + sizeof(len), value);

	return sock_Send(theSocket, msg, total);
}

int
sock_Read(Ptr2Socket* theSocket, char* msg, int length)
{
	if (sock_IsValid(theSocket)) {
		int ret;
		int total = 0;
		int sock = theSocket->sock;
		char* msgptr = msg;
	
		if (theSocket->nonblocking && theSocket->timeout) {
			fd_set readfds;
			struct timeval tv;
	
			tv.tv_sec = theSocket->timeout;
			tv.tv_usec = 0;
	
			FD_ZERO(&readfds);
			FD_SET(sock, &readfds);
	
			ret = select(sock + 1, &readfds, 0, 0, &tv);
	
			if (ret > 0) {
				assert(FD_ISSET(sock, &readfds));
			} else if (ret == 0) {
				/* timed out - nothing is readable */
				return 0;
			} else {
				/* failed to select */
				message("Error: sock_Read select error");
				return 0;
			}
		}
	
		ret = recv(sock, msg, length, 0);
	
		if (ret <= 0) {
			if (sockerrno != WOULDBLOCK) {
				message("Error: sock_Read recv error, closing socket");
				sock_CloseSocket(theSocket);
			}
			return 0;
		} else {
			msgptr += ret;
			total += ret;
			length -= ret;
	
			while (length > 0) {
#if 0
				message("Continuing reading for %d of %d bytes", length, total);
#endif /* 0 */
				MilliSleep(10);
				ret = recv(sock, msgptr, length, 0);
				if (ret <= 0) {
					if (sockerrno != WOULDBLOCK) {
						message("Error: sock_Read recv error, closing socket");
						sock_CloseSocket(theSocket);
						return 0;
					}
					continue;
				}
				msgptr += ret;
				total += ret;
				length -= ret;
			}
		}
		return total;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_Read %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

char*
sock_ReadString(Ptr2Socket* theSocket)
{
	static char* value = NULL;
	unsigned short len;
	int ret = sock_Read(theSocket, (char*)&len, sizeof(len));

	if (ret == 0)
		return NULL;

	len = htons(len);

	if (value) {
		xfree(value);
	}
	value = xmalloc(len + 1);

	ret = sock_Read(theSocket, value, len);
	if (ret == 0)
		return NULL;

	value[len] = 0; /* null terminate string */

	return value;
}

int
sock_GetNonBlocking(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		return theSocket->nonblocking;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_GetNonBlocking %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_SetNonBlocking(Ptr2Socket* theSocket, int flag)
{
	if (sock_IsValid(theSocket)) {
		int sock = theSocket->sock;
		
		mySetSocketNonBlocking(sock, flag);
		theSocket->nonblocking = flag;
		if (socket_messages) {
			message("Set socket %d to %sblocking", sock, flag ? "non" : "");
		}
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_SetNonBlocking %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_GetTimeOut(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		return theSocket->timeout;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_GetTimeOut %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_SetTimeOut(Ptr2Socket* theSocket, int seconds)
{
	if (sock_IsValid(theSocket)) {
		int sock = theSocket->sock;
		
		theSocket->timeout = seconds;
		if (socket_messages) {
			message("Set socket %d to timeout in %d seconds", sock, seconds);
		}
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_SetTimeOut %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_GetBufferSize(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		int bufsize;
		int sizebufsize;
		int ret = 0;
		int sock = theSocket->sock;
		int setret = getsockopt(sock, SOL_SOCKET, SO_SNDBUF, (char*)&bufsize, &sizebufsize);
		
		if (setret != 0) {
			message("Error: sock_GetBufferSize getsockopt error");
		} else {
			ret = bufsize;
		}
		return ret;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_GetBufferSize %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_SetBufferSize(Ptr2Socket* theSocket, int bytes)
{
	if (sock_IsValid(theSocket)) {
		int sock = theSocket->sock;
		int bufsize = bytes;
		int setret = setsockopt(sock, SOL_SOCKET, SO_SNDBUF, (char*)&bufsize, sizeof(bufsize));

		if (setret != 0) {
			message("Error: sock_SetBufferSize setsockopt error");
		}
		if (socket_messages) {
			message("Set socket %d buffer size to %d bytes", sock, bytes);
		}
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_SetBufferSize %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

char*
sock_GetName(Ptr2Socket* theSocket)
{
	if (theSocket == NULL) {
		return "<null>";
	}
	return theSocket->name;
}

char*
sock_GetHost(Ptr2Socket* theSocket)
{
	if (theSocket == NULL) {
		return "<null>";
	}
	return theSocket->host;
}

int
sock_GetNum(Ptr2Socket* theSocket)
{
	if (theSocket == NULL) {
		return INVALID_SOCKET;
	}
	return theSocket->sock;
}

char*
sock_MachineName()
{
	static char thishost[256];

	gethostname(thishost, 256);
	return thishost;
}

Ptr2Socket*
String2Sock(char* str)
{
	return NULL;	// Can't really make one from a string
}
