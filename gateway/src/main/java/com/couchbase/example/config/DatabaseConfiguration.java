package com.couchbase.example.config;

import io.github.jhipster.config.JHipsterConstants;

import com.couchbase.client.java.Bucket;
import com.github.couchmove.Couchmove;
import com.couchbase.example.repository.CustomN1qlCouchbaseRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.convert.CouchbaseCustomConversions;
import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
import org.springframework.data.couchbase.repository.auditing.EnableCouchbaseAuditing;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@Profile("!" + JHipsterConstants.SPRING_PROFILE_CLOUD)
@EnableCouchbaseRepositories(repositoryBaseClass = CustomN1qlCouchbaseRepository.class, basePackages = "com.couchbase.example.repository")
@Import(value = CouchbaseAutoConfiguration.class)
@EnableCouchbaseAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    public ValidatingCouchbaseEventListener validatingCouchbaseEventListener() {
        return new ValidatingCouchbaseEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean(name = BeanNames.COUCHBASE_CUSTOM_CONVERSIONS)
    public CouchbaseCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(ZonedDateTimeToLongConverter.INSTANCE);
        converters.add(NumberToLocalDateTimeConverter.INSTANCE);
        converters.add(BigIntegerToStringConverter.INSTANCE);
        converters.add(StringToBigIntegerConverter.INSTANCE);
        converters.add(BigDecimalToStringConverter.INSTANCE);
        converters.add(StringToBigDecimalConverter.INSTANCE);
        converters.add(StringToByteConverter.INSTANCE);
        return new CouchbaseCustomConversions(converters);
    }

    @Bean
    public Couchmove couchmove(Bucket couchbaseBucket) {
        log.debug("Configuring Couchmove");
        Couchmove couchMove = new Couchmove(couchbaseBucket, "config/couchmove/changelog");
        couchMove.migrate();
        return couchMove;
    }

    /**
     * Simple singleton to convert {@link ZonedDateTime}s to their {@link Long} representation.
     */
    @WritingConverter
    public enum ZonedDateTimeToLongConverter implements Converter<ZonedDateTime, Long> {

        INSTANCE;

        @Override
        public Long convert(ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant()).getTime();
        }
    }

    /**
     * Simple singleton to convert from {@link Number} {@link BigDecimal} representation.
     */
    @ReadingConverter
    public enum NumberToLocalDateTimeConverter implements Converter<Number, ZonedDateTime> {

        INSTANCE;

        @Override
        public ZonedDateTime convert(Number source) {
            return source == null ? null : ZonedDateTime.ofInstant(new Date(source.longValue()).toInstant(), ZoneId.systemDefault());
        }

    }

    /**
     * Simple singleton to convert {@link BigDecimal}s to their {@link String} representation.
     */
    @WritingConverter
    public enum BigDecimalToStringConverter implements Converter<BigDecimal, String> {
        INSTANCE;

        public String convert(BigDecimal source) {
            return source == null ? null : source.toString();
        }
    }

    /**
     * Simple singleton to convert from {@link String} {@link BigDecimal} representation.
     */
    @ReadingConverter
    public enum StringToBigDecimalConverter implements Converter<String, BigDecimal> {
        INSTANCE;

        public BigDecimal convert(String source) {
            return StringUtils.hasText(source) ? new BigDecimal(source) : null;
        }
    }

    /**
     * Simple singleton to convert {@link BigInteger}s to their {@link String} representation.
     */
    @WritingConverter
    public enum BigIntegerToStringConverter implements Converter<BigInteger, String> {
        INSTANCE;

        public String convert(BigInteger source) {
            return source == null ? null : source.toString();
        }
    }

    /**
     * Simple singleton to convert from {@link String} {@link BigInteger} representation.
     */
    @ReadingConverter
    public enum StringToBigIntegerConverter implements Converter<String, BigInteger> {
        INSTANCE;

        public BigInteger convert(String source) {
            return StringUtils.hasText(source) ? new BigInteger(source) : null;
        }
    }

    /**
     * Simple singleton to convert from {@link String} {@link byte[]} representation.
     */
    @ReadingConverter
    public enum StringToByteConverter implements Converter<String, byte[]> {
        INSTANCE;

        @Override
        public byte[] convert(String source) {
            return Base64.decodeBase64(source);
        }
    }
}
