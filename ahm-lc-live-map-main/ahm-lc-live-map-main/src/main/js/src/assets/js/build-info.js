var build = {
    name: 'build.name - Angular',
    artifact: 'build.artifact',
    group: 'build.group',
    version: 'build.version',
    time: 'build.time',
    node: { version: 'build.node.version' },
    npm: { version: 'build.npm.version' },
    java: { version: 'build.java.version' }
};

(function (window) {
    window.__build = build;
}(this));
