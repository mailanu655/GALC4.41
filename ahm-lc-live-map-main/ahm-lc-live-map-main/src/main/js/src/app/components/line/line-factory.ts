import { LineConfig } from "../line-config/line-config";
import { Aap1Config } from "../line-config/line-config-aap1";
import { Aap2Config } from "../line-config/line-config-aap2";
import { ElpConfig } from "../line-config/line-config-elp";
import { Hcm1Config } from "../line-config/line-config-hcm1";
import { Hcm2Config } from "../line-config/line-config-hcm2";
import { HdmcConfig } from "../line-config/line-config-hdmc";
import { IapConfig } from "../line-config/line-config-iap";
import { IpuSubConfig } from "../line-config/line-config-ipusub";
import { Map1Config } from "../line-config/line-config-map1";
import { Map2Config } from "../line-config/line-config-map2";
import { Axis, BodyLocation, Label, LandMark, Line, Section } from "./line-model";

export class LineFactory {

    public create(site: string): Line {
        let config: LineConfig;
        if (site === 'aap1') {
            config = new Aap1Config();
        } else if (site === 'aap2') {
            config = new Aap2Config();
        } else if (site === 'elp') {
            config = new ElpConfig();
        } else if (site === 'hcm1') {
            config = new Hcm1Config();
        } else if (site === 'hcm2') {
            config = new Hcm2Config();
        } else if (site === 'hdmc') {
            config = new HdmcConfig();
        } else if (site === 'iap') {
            config = new IapConfig();
        } else if (site === 'map1') {
            config = new Map1Config();
        } else if (site === 'map2') {
            config = new Map2Config();
        } else if (site === 'ipusub') {
            config = new IpuSubConfig();
        } else {
            config = new LineConfig();
        }
        return this.createLine(config);
    }

    // === factory method === //
    protected createLine(config: LineConfig): Line {
        let line: Line = new Line();
        line.config = config;
        this.createAxis(line, config);
        this.createSections(line, config);
        this.createLandMarks(line, config);
        this.createLabels(line, config);
        return line;
    }
    // === implement in sublass === //
    protected createAxis(line: Line, lineConfig: LineConfig): Line {
        let axisConfigs = lineConfig.axis;
        for (let ac of axisConfigs) {
            let axis = new Axis();
            Object.assign(axis, ac);
            line.axis.push(axis);
        }
        return line;
    }

    protected createSections(line: Line, lineConfig: LineConfig): Line {
        let sectionConfigs = lineConfig.sections;
        for (let sc of sectionConfigs) {
            this.createSection(line, lineConfig, sc);
        }
        line.createLocationIx();
        return line;
    };

    protected createLandMarks(line: Line, config: LineConfig): Line {
        for (let lm of config.landMarks) {
            line.landMarks.push(new LandMark(lm));
        }
        return line;
    }
    protected createLabels(line: Line, config: LineConfig): Line {
        for (let lc of config.labels) {
            line.labels.push(new Label(lc.text, lc.x, lc.y));
        }
        return line;
    }

    // === protected api === //
    protected createSection(line: Line, lineConfig: LineConfig, sectionConfig: any): Section {
        let sc = sectionConfig;
        let bodyLocations = this.createBodyLocations(line, lineConfig, sc);
        if (!sc.entryLength) {
            sc.entryLength = lineConfig.entryLength;
        }
        if (!sc.exitLength) {
            sc.exitLength = lineConfig.exitLength;
        }
        if (!sc.c2c) {
            sc.c2c = lineConfig.c2c;
        }
        if (!sc.length) {
            let locationCount = bodyLocations.length;
            if (locationCount > 1) {
                sc.length = (locationCount - 1) * sc.c2c + sc.entryLength + sc.exitLength;
            } else {
                sc.length = sc.entryLength + sc.exitLength;
            }
        }
        if (!sc.width) {
            sc.width = lineConfig.sectionWidth;
        }
        let section = new Section(sc);
        let prevSection = null;
        let sections = line.sections;
        if (sections.length > 0) {
            prevSection = sections[sections.length - 1];
        }

        this.initSection(section, prevSection);

        line.sectionIx.set(section.id, section);
        let blCx0 = null;
        let blCy0 = null;
        if (section.horizontal) {
            if (section.ascending) {
                blCx0 = section.entryx + sc.entryLength;
            } else {
                blCx0 = section.entryx - sc.entryLength;
            }
            blCy0 = section.entryy;
        } else {
            blCx0 = section.entryx;
            if (section.ascending) {
                blCy0 = section.entryy + sc.entryLength;
            } else {
                blCy0 = section.entryy - sc.entryLength;
            }
        }
        let blCx = blCx0;
        let blCy = blCy0;
        let i = 0;
        for (let bl of bodyLocations) {
            if (section.horizontal) {
                if (section.ascending) {
                    blCx = blCx0 + i * sc.c2c;
                } else {
                    blCx = blCx0 - i * sc.c2c;
                }
            } else {
                if (section.ascending) {
                    blCy = blCy0 + i * sc.c2c;
                } else {
                    blCy = blCy0 - i * sc.c2c;
                }
            }
            bl.cx = blCx;
            bl.cy = blCy;
            bl.section = section;
            line.addLocation(bl);
            section.locations.push(bl);
            i++;
        }
        return section;
    }

    protected initSection(section: Section, prevSection: Section | null) {
        if (!section || !prevSection) {
            return;
        }
        if (!section.entryx) {
            section.entryx = prevSection.exitx;
        }
        if (!section.entryy) {
            section.entryy = prevSection.exity;
        }
        if (section.horizontal === null || section.horizontal === undefined) {
            section.horizontal = prevSection.horizontal;
        }
        if (section.ascending === null || section.ascending === undefined) {
            section.ascending = prevSection.ascending;
        }
    }

    createBodyLocations(line: Line, lineConfig: LineConfig, sectionConfig: any): BodyLocation[] {
        let locations: BodyLocation[] = [];
        if (sectionConfig.locations) {
            for (let blConfig of sectionConfig.locations) {
                let bl = new BodyLocation(blConfig.id);
                bl.scale = lineConfig.shapeScale;
                if (blConfig.code != null) {
                    bl.code = blConfig.code;
                } else {
                    bl.code = "" + bl.id;
                }
                locations.push(bl);
            }
            return locations;
        }
        let locationStartId = 1;
        if (lineConfig.locationStartId != null) {
            locationStartId = lineConfig.locationStartId;
        }
        let locs: BodyLocation[] = line.locations;
        let lastBl = null;
        if (locs && locs.length > 0) {
            lastBl = locs[locs.length - 1];
        }
        if (lastBl) {
            locationStartId = lastBl.id + 1;
            locationStartId = Math.floor(locationStartId);
        }
        let startId: number = locationStartId;
        for (let i = 0; i < sectionConfig.locationCount; i++) {
            let blId = startId + i;
            let bl = new BodyLocation(blId);
            bl.scale = lineConfig.shapeScale;
            bl.code = "" + blId;
            locations.push(bl);
        }
        return locations;
    }
}