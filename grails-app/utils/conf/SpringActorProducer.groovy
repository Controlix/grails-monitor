package conf

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import groovy.util.logging.Slf4j

/**
 * An actor producer that lets Spring create the Actor instances.
 */
@Slf4j
public class SpringActorProducer implements IndirectActorProducer {
  final ApplicationContext applicationContext;
  final String actorBeanName;
  final Object[] constructorArguments;

  public SpringActorProducer(ApplicationContext applicationContext,
                             String actorBeanName, Object... constructorArguments) {
    this.applicationContext = applicationContext;
    this.actorBeanName = actorBeanName;
	this.constructorArguments = constructorArguments;
  }

  @Override
  public Actor produce() {
    log.debug "produce() called for actorBeanName: {}", actorBeanName
    return (Actor) applicationContext.getBean(actorBeanName, constructorArguments);
  }

  @Override
  public Class<? extends Actor> actorClass() {
    log.debug "actorClass() called for actorBeanName: {}", actorBeanName
    return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
  }
}