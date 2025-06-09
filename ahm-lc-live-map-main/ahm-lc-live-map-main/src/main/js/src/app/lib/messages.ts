export class MessageBundle {
    private messages: Map<string, string> = new Map<string, string>();
    constructor() {
        this.messages.set('tooltip.station', 'QICS computer location where data entry is conducted');
        this.messages.set('tooltip.new.lot', 'First VIN of each unique KD lot');
        this.messages.set('tooltip.straggler', 'Any VIN that did not enter AF with it’s initial KD lot (includes remakes)');
        this.messages.set('tooltip.qics.defect', 'VIN with an unrepaired QICS defect');

        this.messages.set('tooltip.qics.defect.all', 'Any QICS defects for a VIN');
        this.messages.set('tooltip.qics.defect.weld', 'QICS Defects marked as Weld responsible');
        this.messages.set('tooltip.qics.defect.paint', 'QICS Defects marked as Paint responsible');
        this.messages.set('tooltip.qics.defect.af', 'QICS Defects marked as AF responsible');

        this.messages.set('tooltip.part.issue', 'VIN with an unrepaired torque or scan failure');
        this.messages.set('tooltip.product.hold', 'VIN currently in hold status');
        this.messages.set('tooltip.process.areas', 'line location of various processes (set by plant)');
        this.messages.set('tooltip.tl.areas', 'line location for selected/all team leaders (set by plant)');
        this.messages.set('tooltip.pmqa.issue', 'PMQA Issues');

        this.messages.set('tooltip.search', 'Search Products');
        this.messages.set('tooltip.watch', 'Watch Products');
        this.messages.set('tooltip.clear', 'Clear');

        this.messages.set('tooltip.download.xls', 'Download Data as Excel');
        this.messages.set('tooltip.print', 'Print Data');
    }

    get(key: string) {
        return this.messages.get(key);
    }

    get tooltipDelay(): number {
        return 400;
    }
}
export const messages: MessageBundle = new MessageBundle();