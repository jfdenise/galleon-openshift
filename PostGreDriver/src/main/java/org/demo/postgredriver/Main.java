/*
Copyright 2019 Red Hat, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
                // WARNING: you must copy driver binary jar file to project root dir.
                .addPath("modules/system/layers/base/org/postgresql/main/postgresql-42.2.2.jar", Paths.get("postgresql-42.2.2.jar"));
        creator.install();
    }
}
