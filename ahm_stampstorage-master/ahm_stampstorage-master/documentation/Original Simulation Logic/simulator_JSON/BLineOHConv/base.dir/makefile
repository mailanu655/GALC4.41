########################################################################
#
#	AutoMod Makefile
#	AutoMod Licensee Confidential
#	Copyright (c) 2007-2009 Applied Materials. All rights reserved.
#	Any and all distribution without express written consent is prohibited.
#
#	This makefile was generated with AutoMod 12.3.1, Build: 2152.80
#
#	This makefile was designed to be used with nmake 8.0 (VS2005).
#	Other makefiles can be included by reference to build other libraries
#	along with your model. This makefile will look for a file called 'model.mak'
#	in the model directory (modelname.dir) and include it if it exists. 
#	See the Microsoft documentation for the nmake utility for more information 
#	on how you can write makefiles,	or use Visual Studio to generate them for you.
#
#	There are a few macros in the template for your ease of use:
#	USER_OBJS : a list of objects to be linked in to the final model. These objects will be linked in last.
#	USER_TARGETS : a list of targets to be built when the model is built. The model will be built last.
#	
#
#########################################################################

!IF EXIST ("model.mak")
!	include <model.mak>
!ENDIF # IF EXIST ("model.mak")

!include <$(ASI)\lib\common.mak>

TARGET		=base
DLL_NAME	=$(TARGET)$(DLL_EXTENSION).dll
!IF DEFINED (DEBUG_MSG)
!	MESSAGE Processing $(DEBUG_MSG) build commands for the model $(TARGET)...
!ELSE
!	MESSAGE Processing build commands for the model $(TARGET)...
!ENDIF

GROUP_1 = "base~model.obj" \
	"base~init1.obj" \
	"presslogic.obj" \
	"sortinglogic.obj" \
	"cjson.obj" \
	"socketcomm.obj"

OBJECTS =  $(GROUP_1) 
OBJ_MODULES	= $(OBJECTS) $(USER_OBJS)
SOURCE_FILES	= $(OBJECTS:.obj=.c)

MODELNAME	=..\$(TARGET).exe

#	The model should always be the first target...
model : $(USER_TARGETS) $(DLL_NAME) $(MODELNAME)
	
$(MODELNAME) : $(DLL_NAME)
	@ECHO Generating $(TARGET).exe...
	@COPY $(RUNMODEL) $(MODELNAME)
	
#	Model Files and objects

"base~model.obj" : "base~model.c" $(MODEL_HEADERS)

"base~init1.obj" : "base~init1.c" $(MODEL_HEADERS)

"presslogic.obj" : "presslogic.c" $(MODEL_HEADERS)
	$(CC) $(C_OPTIONS) "presslogic.c"

"sortinglogic.obj" : "sortinglogic.c" $(MODEL_HEADERS)
	$(CC) $(C_OPTIONS) "sortinglogic.c"

"cjson.obj" : "cjson.c" $(MODEL_HEADERS)
	$(CC) $(C_OPTIONS) "cjson.c"

"socketcomm.obj" : "socketcomm.c" $(MODEL_HEADERS)
	$(CC) $(C_OPTIONS) "socketcomm.c"


$(DLL_NAME) : $(OBJ_MODULES)
	@ECHO Linking $(DLL_NAME)...
	@$(LD) $(DLL_OPTIONS) /out:$(DLL_NAME) $(LIB_PATH) $(LIBS) @<<linkfile.txt
$(OBJ_MODULES)
<<
	@$(MT) -manifest $(DLL_NAME).manifest -outputresource:$(DLL_NAME);#2 -nologo

clean :
	@ECHO Cleaning files from the model directory ...
	-del /Q /s *.obj
	-del /Q $(DLL_NAME)
	-del /Q $(DLL_NAME).manifest

rebuild : clean model