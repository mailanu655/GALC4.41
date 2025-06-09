export const DATE_PATTERN: string = 'yyyy-MM-dd HH:mm:ss';

export function beforeExecute(component: any) {
  dismissMessage(component);
  document.body.classList.add('busy-cursor');
  if (component) {
    component.busy = true;
  }
}

export function afterExecute(component: any) {
  document.body.classList.remove('busy-cursor');
  if (component) {
    component.busy = false;
  }
}

export function onError(component: any, error?: any, callback?: any) {
  let msg = 'Failed to execute request !';
  displayErrorMessage(component, msg, 'Close', callback);
  console.error('Error: ' + error);
  console.error('Error: ' + JSON.stringify(error));
}

// === confirm === //
export function confirmAction(message?: string): boolean {
  if (message) {
    return confirm(message);
  } else {
    return confirm('Are you sure ?');
  }
}

// === messaging === //
export function displayMessage(
  component: any,
  message: string,
  action: string,
  duration: number = 3000
) {
  if (component.snackBar) {
    component.snackBar.open(message, action, {
      duration: duration,
      panelClass: ['success-snackbar'],
    });
  }
}

export function displayWarningMessage(
  component: any,
  message: string,
  action: string,
  duration: number = 10000
) {
  if (component.snackBar) {
    component.snackBar.open(message, action, {
      duration: duration,
      panelClass: ['warning-snackbar'],
    });
  }
}

export function displayErrorMessage(
  component: any,
  message: string,
  action: string,
  callback?: any,
  duration: number = 30000
) {
  if (component.snackBar) {
    let snackBarRef = component.snackBar.open(message, action, {
      duration: duration,
      panelClass: ['error-snackbar'],
    });
    if (callback) {
      snackBarRef.afterDismissed().subscribe(() => {
        callback();
      });
    }
  }
}

export function dismissMessage(component: any) {
  if (component.snackBar) {
    component.snackBar.dismiss();
  }
}

export function formatDate(dateString: string) {
  const months = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
  const date = new Date(dateString);

  const year = date.getFullYear();
  const month = months[date.getMonth()];
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');
  const milliseconds = date.getMilliseconds().toString().padStart(3, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}.${milliseconds}`;
}
