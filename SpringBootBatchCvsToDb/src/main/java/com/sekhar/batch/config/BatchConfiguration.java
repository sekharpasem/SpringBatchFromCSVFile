package com.sekhar.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.sekhar.batch.listener.JobCompletionNotificationListener;
import com.sekhar.batch.model.Feed;
import com.sekhar.batch.processor.FeedItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<Feed> reader() {
        FlatFileItemReader<Feed> reader = new FlatFileItemReader<Feed>();
        reader.setResource(new ClassPathResource("feeds.csv"));
        reader.setLineMapper(new DefaultLineMapper<Feed>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "feedName", "description"});
                setDelimiter("|");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Feed>() {{
                setTargetType(Feed.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FeedItemProcessor processor() {
        return new FeedItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Feed> writer() {
        JdbcBatchItemWriter<Feed> writer = new JdbcBatchItemWriter<Feed>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Feed>());
        writer.setSql("INSERT INTO feed (feed_name, description) VALUES (:feedName, :description)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Feed, Feed> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}