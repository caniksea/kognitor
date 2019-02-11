package services.actors.tasks

import play.api.inject.{SimpleModule, bind}
import services.actors.StartActors

class TasksModule extends SimpleModule(bind[StartActors].toSelf.eagerly())
