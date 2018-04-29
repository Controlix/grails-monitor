package conf

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

/**
 * An actor producer that lets Spring create the Actor instances.
 */
public class SpringActorProducer implements IndirectActorProducer {
  final ApplicationContext applicationContext;
  final String actorBeanName;

  public SpringActorProducer(ApplicationContext applicationContext,
                             String actorBeanName) {
    this.applicationContext = applicationContext;
    this.actorBeanName = actorBeanName;
  }

  @Override
  public Actor produce() {
    println "produce() called for actorBeanName: ${actorBeanName}"
    return (Actor) applicationContext.getBean(actorBeanName);
  }

  @Override
  public Class<? extends Actor> actorClass() {
    println "actorClass() called for actorBeanName: ${actorBeanName}"
    return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
  }
}