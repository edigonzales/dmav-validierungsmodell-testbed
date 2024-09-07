package ch.interlis.testbed;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicDependencyLoader {
    private final RepositorySystem system;
    private final RepositorySystemSession session;
    private final RemoteRepository centralRepo;
    private final RemoteRepository interlisRepo;

    public DynamicDependencyLoader() {
        system = newRepositorySystem();
        session = newSession(system);
        centralRepo = new RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build();
        interlisRepo = new RemoteRepository.Builder("interlis", "default", "https://jars.interlis.ch/").build();
    }

    public void loadDependency(String groupId, String artifactId, String version) throws Exception {
        DefaultArtifact artifact = new DefaultArtifact(groupId, artifactId, "jar", version);
        
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.RUNTIME));
        collectRequest.setRepositories(Arrays.asList(centralRepo, interlisRepo));

        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, DependencyFilterUtils.classpathFilter(JavaScopes.RUNTIME));

        DependencyResult dependencyResult = system.resolveDependencies(session, dependencyRequest);

        List<File> files = dependencyResult.getArtifactResults().stream()
            .map(artifactResult -> artifactResult.getArtifact().getFile())
            .collect(Collectors.toList());
        
        addToClasspath(files);
        
        System.out.println("Added " + files.size() + " dependencies to the classpath.");
    }
    
    @SuppressWarnings("deprecation")
    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        return locator.getService(RepositorySystem.class);
    }

    private static RepositorySystemSession newSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository("target/local-repo");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        return session;
    }
    
    private static void addToClasspath(List<File> jarFiles) throws Exception {
        URL[] urls = new URL[jarFiles.size()];

        for (int i = 0; i < jarFiles.size(); i++) {
            urls[i] = jarFiles.get(i).toURI().toURL();
        }
        new MyAppClassLoader(urls, MyAppClassLoader.class.getClass().getClassLoader());        
    }
}