module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires org.json;
    requires java.logging;
    requires java.desktop;
    requires java.net.http;
    exports org.WhatIsTheBpm;
    exports org.WhatIsTheBpm.Controllers;
    exports org.WhatIsTheBpm.KeyHandling;
    exports org.WhatIsTheBpm.Bpm;
}