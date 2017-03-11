package com.spring.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableBatchProcessing
@Import({BatchScheduler.class})
public class BatchConfig {
	
	private static final Logger log = LoggerFactory
			.getLogger(BatchConfig.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss");
  
    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
      
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }
  
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String> chunk(1)
                .reader(new Reader())
                .processor(new Processor())
                .writer(new Writer())
                .build();
    }
    
    @Scheduled(fixedRate = 5000)
	public void reportCurrentTime() throws Exception{
		log.info("The time is now {}", dateFormat.format(new Date()));
		JobParameters param = new JobParametersBuilder().addString("JobID",
                String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution =  jobLauncher.run(job(), param);
		System.out.println("Job Execution Status: " + execution.getStatus());
	}
    
    
}