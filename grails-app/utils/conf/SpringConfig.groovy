package conf

import org.springframework.context.annotation.Import

@Import([AkkaSpringConfiguration, PrometheusConfiguration])
class SpringConfig {

}
