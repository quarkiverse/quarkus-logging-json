package io.quarkiverse.loggingjson.deployment.testutil;

import io.quarkus.bootstrap.model.PathsCollection;
import io.quarkus.maven.dependency.ArtifactCoords;
import io.quarkus.maven.dependency.ArtifactKey;
import io.quarkus.maven.dependency.Dependency;
import io.quarkus.maven.dependency.ResolvedDependency;
import io.quarkus.paths.PathCollection;
import io.quarkus.paths.PathList;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class DefaultDependency implements ResolvedDependency {

    protected PathsCollection paths;
    private String scope = "compile";
    private int flags = 0;
    private String classifier = DEFAULT_CLASSIFIER;
    private String groupId = "io.quarkus";
    private String artifactId = "quarkus-logging-json";
    private String version = System.getProperty("test.quarkus.version");
    private String type = TYPE_JAR;

    public DefaultDependency() {
    }

    public DefaultDependency(String artifactId) {
        this.artifactId = artifactId;
    }

    public DefaultDependency(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public DefaultDependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public DefaultDependency(String groupId, String artifactId, String version, String type) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = type;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getClassifier() {
        return classifier;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public ArtifactKey getKey() {
        return ArtifactKey.of(groupId, artifactId, classifier, type);
    }

    @Override
    public PathCollection getResolvedPaths() {
        return paths == null ? null : PathList.from(paths);
    }

    @Override
    public Collection<ArtifactCoords> getDependencies() {
        return List.of();
    }

    public PathsCollection getPaths() {
        return paths;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPaths(PathsCollection paths) {
        this.paths = paths;
    }

    public void setPath(Path path) {
        setPaths(PathsCollection.of(path));
    }
}
