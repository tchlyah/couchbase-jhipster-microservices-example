package com.couchbase.example.brewery.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import org.assertj.core.util.Lists;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;
import org.testcontainers.couchbase.CouchbaseContainer;

import java.util.List;

@Configuration
public class DatabaseTestConfiguration extends AbstractCouchbaseConfiguration {

    private CouchbaseProperties couchbaseProperties;

    private static CouchbaseContainer<? extends CouchbaseContainer<?>> couchbaseContainer;

    public DatabaseTestConfiguration(CouchbaseProperties couchbaseProperties) {
        this.couchbaseProperties = couchbaseProperties;
    }

    @Override
    @Bean(destroyMethod = "", name = BeanNames.COUCHBASE_ENV)
    public CouchbaseEnvironment couchbaseEnvironment() {
        return getCouchbaseContainer().getCouchbaseEnvironment();
    }

    @Override
    public Cluster couchbaseCluster() throws Exception {
        return getCouchbaseContainer().getCouchbaseCluster();
    }

    @Override
    protected List<String> getBootstrapHosts() {
        return Lists.newArrayList(getCouchbaseContainer().getContainerIpAddress());
    }

    @Override
    protected String getBucketName() {
        return couchbaseProperties.getBucket().getName();
    }

    @Override
    protected String getBucketPassword() {
        return couchbaseProperties.getBucket().getPassword();
    }

    @Override
    protected CouchbaseConfigurer couchbaseConfigurer() {
        return this;
    }

    private CouchbaseContainer<? extends CouchbaseContainer<?>> getCouchbaseContainer() {
        if (couchbaseContainer != null) {
            return couchbaseContainer;
        }
        couchbaseContainer = new CouchbaseContainer<>("couchbase/server:5.5.1");
        couchbaseContainer
            .withNewBucket(DefaultBucketSettings.builder()
                .name(getBucketName())
                .password(getBucketPassword())
                .type(BucketType.COUCHBASE)
                .quota(100)
                .build());
        couchbaseContainer.start();
        return couchbaseContainer;
    }
}
