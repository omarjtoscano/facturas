package com.ibm.facturas;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FacturasApplication {

    private static volatile boolean jobEjecutado = false;

    public static void main(String[] args) {
        SpringApplication.run(FacturasApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(JobLauncher jobLauncher, Job facturaJob) {
        return args -> {
            synchronized (FacturasApplication.class) {
                if (jobEjecutado) {
                    System.out.println("El job ya se ejecutó. Ignorando...");
                    return;
                }

                if (args.length == 0) {
                    System.out.println("Debe proporcionar parámetros (fecha=YYYY-MM-DD y outputFile=path)");
                    return;
                }

                var builder = new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis());

                for (String arg : args) {
                    if (arg.startsWith("fecha=")) {
                        builder.addString("fecha", arg.substring("fecha=".length()));
                    } else if (arg.startsWith("outputFile=")) {
                        builder.addString("outputFile", arg.substring("outputFile=".length()));
                    }
                }

                jobLauncher.run(facturaJob, builder.toJobParameters());
            }
        };
    }
}
