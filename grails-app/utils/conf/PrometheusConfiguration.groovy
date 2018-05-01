package conf;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.hotspot.MemoryPoolsExports;
import io.prometheus.client.hotspot.StandardExports;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;

@Configuration
@EnablePrometheusEndpoint
class PrometheusConfiguration {

	@Autowired
	private MetricRegistry dropwizardMetricRegistry;

	@PostConstruct
	public void registerPrometheusCollectors() {
		CollectorRegistry.defaultRegistry.clear();
		new StandardExports().register();
		new MemoryPoolsExports().register();
		new DropwizardExports(dropwizardMetricRegistry).register();
	}
}
