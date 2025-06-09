export class Resource {
    constructor(private _name: string, private _label: string, private _path: string) {
    }

    get name() {
        return this._name;
    }

    get label() {
        return this._label;
    }



    get path() {
        return this._path;
    }

    get createPath() {
        return this._path + "/create";
    }

    get viewPath() {
        return this._path + "/:id";
    }

    get createRole() {
        return this._name + '_create';
    }

    get readRole() {
        return this._name + '_read';
    }

    get updateRole() {
        return this._name + '_update';
    }

    get deleteRole() {
        return this._name + '_delete';
    }

    static readonly CONFIGURATION: Resource = new Resource('configuration', 'Configuration', 'configurations');
    static readonly PROFILE: Resource = new Resource('profile', 'Profile', 'profiles');
    static readonly JOB: Resource = new Resource('job', 'Job', 'jobs');
}
