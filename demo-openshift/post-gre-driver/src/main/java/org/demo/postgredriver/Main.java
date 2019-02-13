/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demo.postgredriver;

import java.nio.file.Paths;
import org.jboss.galleon.config.ConfigModel;
import org.jboss.galleon.config.FeatureConfig;
import org.jboss.galleon.config.FeaturePackConfig;
import org.jboss.galleon.creator.FeaturePackCreator;
import org.jboss.galleon.universe.FeaturePackLocation;
import org.jboss.galleon.universe.UniverseSpec;
import org.jboss.galleon.universe.galleon1.LegacyGalleon1RepositoryManager;
import org.jboss.galleon.universe.galleon1.LegacyGalleon1Universe;

/**
 *
 * @author jdenise@redhat.com
 */
public class Main {
    public static void main(String[] args) throws Exception {
        FeaturePackCreator creator = new FeaturePackCreator();
        FeaturePackLocation loc = LegacyGalleon1Universe.toFpl("org.jboss.galleon.demo", "postgresql", "1.0");
        creator.addArtifactResolver(LegacyGalleon1RepositoryManager.newInstance(Paths.get("local-repo")));
        UniverseSpec spec = new UniverseSpec("maven", "org.jboss.universe:community-universe");
        FeaturePackConfig fpConfig = FeaturePackConfig.builder(new FeaturePackLocation(spec,"wildfly","current", null, null)).
                setInheritConfigs(false).setInheritPackages(false).build();
        FeatureConfig ds = FeatureConfig.newConfig("subsystem.datasources");
        FeatureConfig driver = FeatureConfig.newConfig("subsystem.datasources.jdbc-driver")
                .setParam("jdbc-driver", "postgresql").
                setParam("driver-name", "postgresql").
                setParam("driver-module-name", "org.postgresql").
                setParam("driver-class-name","org.postgresql.Driver");
        ds.addFeature(driver);
        ConfigModel config = ConfigModel.builder("standalone", "standalone.xml").
                addFeature(ds).build();
        creator.newFeaturePack(loc.
                getFPID()).addDependency(fpConfig).addConfig(config).newPackage("org.postgresql", true)
                .addPath("modules/system/layers/base/org/postgresql/main/module.xml", Paths.get("module.xml"))
                .addPath("modules/system/layers/base/org/postgresql/main/postgresql-42.2.2.jar", Paths.get("postgresql-42.2.2.jar"));
        creator.install();
    }
}
