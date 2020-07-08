package br.com.dcc.springbatchexamples.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class HelloWorldConfiguration {

	@Bean
	@StepScope
	public Tasklet helloWorldTasklet(@Value("#{jobParameters['message']}") String message) {
		return (contribution, chunkContext) -> {
			log.info("Execution of step with parameter message = {}", message);
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public Step helloWorldStepWithParam(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("Step")
				.tasklet(helloWorldTasklet(null))
				.build();
	}

	@Bean
	public Job helloWorldJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("HelloWorldJob")
				.start(helloWorldStepWithParam(stepBuilderFactory))
				.build();
	}

}
