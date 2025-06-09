var build = {
    name: 'build.name - Angular',
    artifact: 'build.artifact',
    group: 'build.group',
    version: 'build.version',
    time: 'build.time',
    java: { version: 'build.java.version' }
};

(function (window) {
    window.__build = build;
}(this));
