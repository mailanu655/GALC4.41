import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription, interval } from 'rxjs';
import { ConfirmComponent } from 'src/app/components/confirm/confirm.component';
import { ObjectUtilities } from './common-object';

export abstract class BaseComponent {

    private _processing: boolean;
    private updateSubscription: Subscription;

    constructor(private _snackBar: MatSnackBar) {
    }

    // === handlers === //
    onRefresh() {
        console.log("Refreshing - todo in subclass !");
    }

    onError(error?: any) {
        let msg: string = "Failed to execute request !";
        let callback = null;
        if (error) {
            if (error.status === 401) {
                msg = "Your session expired, please login and try again (401) !"
                callback = function () { window.location.href = window.location.origin };
            } else if (error.status === 403) {
                msg = "You have no permission to execute this request (403) !"
            }
            console.log("Error: " + JSON.stringify(error));
        }
        this.displayErrorMessage(msg, "OK", callback)
        this.afterExecute();
    }
    // === confirm === //
    confirmAction(message?: string): boolean {
        if (message) {
            return confirm(message);
        } else {
            return confirm("Are you sure ?");
        }
    }

    confirmActionDialog(dialog: MatDialog, message?: any, minWidth?: number): Promise<boolean> {
        if (!message) {
            message = "Are you sure ?";
        }
        if (!minWidth) {
            minWidth = 300;
        }
        let data = { message: message };
        const dialogRef = dialog.open(ConfirmComponent, {
            minWidth: minWidth,
            data: data
        });
        const result: Promise<any> = dialogRef.afterClosed().toPromise();
        return result;
    }
    // === messaging === //
    displaySuccessMessage(message: string, action: string) {
        if (this.snackBar) {
            this.snackBar.open(message, action, {
                duration: 3000,
                panelClass: ['success-snackbar']
            });
        }
    }

    displayWarningMessage(message: string, action: string) {
        if (this.snackBar) {
            this.snackBar.open(message, action, {
                duration: 10000,
                panelClass: ['warning-snackbar']
            });
        }
    }

    displayErrorMessage(message: string, action: string, callback?: any) {
        if (this.snackBar) {
            let snackBarRef = this.snackBar.open(message, action, {
                duration: 30000,
                panelClass: ['error-snackbar']
            });
            if (callback) {
                snackBarRef.afterDismissed().subscribe(() => {
                    callback();
                });
            }
        }
    }

    beforeExecute() {
        document.body.classList.add('busy-cursor');
        this.processing = true;
    }

    afterExecute() {
        document.body.classList.remove('busy-cursor');
        this.processing = false;
    }

    // === timer === //
    setTimedUpdate(value: number) {
        if (value) {
            value = value * 1000;
        } else {
            value = 60 * 1000;
        }
        this.updateSubscription = interval(value).subscribe(res => {
            this.timedUpdate();
        });
    }

    cancelTimedUpdate() {
        if (this.updateSubscription) {
            this.updateSubscription.unsubscribe();
        }
    }

    resetTimedUpdate(value: number) {
        this.cancelTimedUpdate();
        this.setTimedUpdate(value);
    }

    timedUpdate() {
        this.onRefresh();
    }

    // === utility === //
    getProperty(item: any, propertyName: string) {
        return ObjectUtilities.getProperty(item, propertyName);
    }

    // === get/set === //
    get snackBar(): MatSnackBar {
        return this._snackBar;
    }

    get processing(): boolean {
        return this._processing;
    }

    set processing(processing: boolean) {
        this._processing = processing;
    }

    get mobile() {
        return /Android|webOS|iPhone|iPad|iPod|Macintosh|BlackBerry|BB|PlayBook|IEMobile|Windows Phone|Kindle|Silk|Opera Mini/i.test(navigator.userAgent);
    }

    get portrait() {
        if (window.innerHeight > window.innerWidth) {
            return true;
        } else {
            return false;
        }
    }
}