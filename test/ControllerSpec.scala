import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import com.eclipsesource.json.JsonObject
import model.Controller

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends Specification {

  "Controller" should {
    
    var firstResponse = new JsonObject().add("controllerId", 9218).add("controllerName", "Test Controller")
    var secondResponse = new JsonObject().add("allowRun", false).add("running", false).add("progNumber", 1111111)
    var controller = Controller.makeController(firstResponse, "Location #1", "66342", secondResponse)

    "name correct" in {
      controller.name must beEqualTo("Test Controller")
    }
    
    "id correct" in {
      controller.id must beEqualTo(9218)
    }
    
    "locName correct" in {
      controller.locName must beEqualTo("Location #1")
    }
    
    "locID correct" in {
      controller.locID must beEqualTo("66342")
    }
    
    "running correct" in {
      controller.running must beEqualTo(false)
    }
    
    "allowRun correct" in {
      controller.allowRun must beEqualTo(false)
    }
    
    "progNumber correct" in {
      controller.programNumber must beEqualTo(1111111)
    }
  }
}
