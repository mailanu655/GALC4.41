import { Component, Input } from '@angular/core';
import { SpecFormat } from 'src/app/models/spec-format';

@Component({
    selector: 'spec-mask',
    templateUrl: './spec-mask.component.html',
    styleUrls: ['./spec-mask.component.css']
})
export class SpecMaskComponent {

    @Input()
    mask: string = '';

    @Input()
    specFormat: SpecFormat | undefined;

    get formattedMask() {
        return this.specFormat.formatMask(this.mask);
    }

    get data(): any[] {
        let tokens = this.specFormat!.tokens;
        let formattedMask = this.specFormat.formatMask(this.mask);
        let ar = formattedMask!.split('');
        let tokenIx = 0;
        let token = tokens[tokenIx];
        let class1 = 'odd';
        let class2 = 'even';
        let clazz = class1;
        let data: any[] = [];
        for (let i = 0; i < ar.length; i++) {
            if (i >= token.endIx) {
                clazz = clazz === class1 ? class2 : class1;
                tokenIx++;
                token = tokens[tokenIx];
            }
            let ix = i + 1;
            let c = ar[i];
            data.push({ meta: { text: ix, class: clazz }, value: { text: c, class: clazz } });
        }
        return data;
    }
}
