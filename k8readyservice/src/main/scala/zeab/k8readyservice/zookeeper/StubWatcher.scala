package zeab.k8readyservice.zookeeper

//Imports
import org.apache.zookeeper.{WatchedEvent, Watcher}

//This just stubs out the watcher for the moment... mostly so i stop getting that error
class StubWatcher extends Watcher {
  override def process(event: WatchedEvent): Unit = ()
}