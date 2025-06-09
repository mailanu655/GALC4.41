import { Component, Inject, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SpecFormat, TokenFormat } from 'src/app/models/spec-format';
@Component({
    selector: 'spec-mask-select',
    templateUrl: './spec-mask-select.component.html',
    styleUrls: ['./spec-mask-select.component.css']
})
export class SpecMaskSelectComponent {

    @Input()
    specFormat!: SpecFormat;

    @Input()
    specs: string[] = [];

    options: any = {};
    
    values: any = {};

    constructor(public dialogRef: MatDialogRef<SpecMaskSelectComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
        
        if (data?.specFormat) {
            this.specFormat = data.specFormat;
        }
        if (data?.specs) {
            this.specs = data.specs;
        }

        
    }

    // === init === //
    ngOnInit() {
        if (this.specFormat.tokens.length > 0) {
            let firstToken = this.specFormat.tokens[0];
            this.options[firstToken.name] = this.getValues(this.specFormat, firstToken.name, '');
        }
        if (this.data.mask) {
            this.mask = this.data.mask;
        }
    }

    // === action handlers === //
    onSelect() {
        this.dialogRef.close(this.mask);
    }

    onCancel() {
        this.dialogRef.close(null);
    }

    // === event handlers === //
    valueChanged(ix: number) {
        let tokenCount = this.specFormat.tokens.length;
        if (ix === tokenCount - 1) {
            return;
        }
        let nextIx = ix + 1;
        for (let i = tokenCount - 1; i > nextIx; i--) {
            let token = this.specFormat.tokens[i];
            delete this.values[token.name];
            this.options[token.name] = [];
        }
        let nextToken = this.specFormat.tokens[nextIx];
        delete this.values[nextToken.name];
        this.options[nextToken.name] = this.getValues(this.specFormat, nextToken.name, this.mask);
    }

    // === data === //
    getValues(specFormat: SpecFormat, tokenName: string, mask: string): string[] {
        let tokenFormat = specFormat.getTokenFormat(tokenName);
        let values: string[] = [];
        if (!tokenFormat) {
            return values;
        }
        let regExp = specFormat.maskToRegEx(mask);
        for (let spec of this.specs) {
            if (spec.match(regExp)) {
                let value = specFormat.parseToken(spec, tokenName);
                if (values.indexOf(value) < 0) {
                    values.push(value);
                }
            }
        }
        values.sort();
        values = ["".padEnd(tokenFormat.length, SpecFormat.WILD_CARD), ...values];
        return values;
    }

    // === get/set=== //
    get mask() {
        let mask = '';
        for (let token of this.specFormat.tokens) {
            let value = this.values[token.name];
            if (value === undefined || value === null) {
                return mask;
            }
            mask = mask + value;
        }
        return mask;
    }

    set mask(mask: string) {
        this.values = {};
        let tokenCount = this.specFormat.tokens.length;
        let lastIx = tokenCount - 1;
        for (let ix = 0; ix < tokenCount; ix++) {
            let token = this.specFormat.tokens[ix];
            let value = this.specFormat.parseToken(mask, token.name);
            if (value === undefined || value === null) {
                return;
            }
            let options = this.options[token.name];
            if (!options || options.length === 0) {
                return;
            }
            if (options.indexOf(value) > -1) {
                this.values[token.name] = value;
                if (ix < lastIx) {
                    this.valueChanged(ix);
                }
            }
        }
    }

    get lastTokenSelected() {
        let token = this.specFormat.tokens[this.specFormat.tokens.length - 1];
        let value = this.values[token.name];
        if (value !== undefined && value !== null && value != '') {
            return true;
        } else {
            return false;
        }
    }

    getFlexFactor(token: TokenFormat) {
        let constant = 5;
        let factor = (token.length + constant) / (this.specFormat.length + constant);
        return factor;
    }
}
