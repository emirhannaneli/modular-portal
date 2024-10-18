### Modular Portal

**Proje Adı:** Modüler Portal  
**Project Name:** Modular Portal

**Amaç:** Tek proje üzerindeki kod yükünü azaltıp sadece gerekli özelliklerin bulunduğu bir ekosistemi çok basit bir şekilde sunmak.  
**Purpose:** Reduce code load on a single project and provide an ecosystem with only the necessary features in a very simple way.

---

### Proje Detayı / Project Overview

**Modüler Portal**, büyük uygulamaları modüler hale getirmek için esnek ve eklenti tabanlı bir mimari sağlar. Bu yapı, ana projedeki kod yükünü azaltmaya ve temel sistemin şişmesine neden olmadan özellikleri kolayca ekleyip kaldırmaya yardımcı olur.  
The **Modular Portal** project is designed to modularize large applications by providing a flexible, plug-in-based architecture. This structure helps reduce the overall codebase of the main project and makes it easier to add or remove features as needed without bloating the core system.

#### Temel Özellikler / Key Features:
1. **Dinamik Modül Enjeksiyonu / Dynamic Module Injection:**
   Proje, **Reflection** kullanarak Spring Boot'un contextinde dinamik olarak bean'lerin enjekte edilmesini ve manipüle edilmesini sağlar. Bu, uygulamanın bağımlılıklarını ve davranışlarını yönetirken daha fazla esneklik sunar.  
   The project uses **Reflection** in Spring Boot's context to dynamically inject and manipulate beans. This allows for greater flexibility when managing dependencies and behaviors in the application.

2. **Eklenti Sistemi / Plugin System:**
   Sistem bir **eklenti (add-on) mimarisi** ile tasarlanmıştır:
   - Ana sunucu olan `core.jar`, bağımsız bir uygulama olarak başlatılır.
   - `core.jar` dizininde, `addons` klasörü yer alır. Bu klasöre yerleştirilen herhangi bir JAR dosyası, ana sunucu tarafından otomatik olarak yüklenir.
   - Bu JAR'ların ana uygulamaya dahil edilebilmesi için belirli bir yapıya uygun olmaları ve core'un API'sini kullanmaları gerekmektedir.
   - The system is designed with an **add-on (plugin) architecture**:
      - The main server, referred to as `core.jar`, is booted up as a standalone application.
      - Inside the root directory of `core.jar`, there is an `addons` folder. Any JAR files placed in this folder are automatically loaded by the main server.
      - For these JARs to be loaded and included in the main application, they must comply with a specific structure and must use the core's API.

3. **Core API Gereksinimi / Core API Requirement:**
   Eklentiler, uygulama contextine enjekte edilebilmek için core API ile etkileşime girmelidir. Eğer bir JAR bu gereksinimi karşılamazsa, sistem tarafından reddedilir. Bu, ana sunucunun bütünlüğünü ve kararlılığını korur.  
   Add-ons must interact with the core API to ensure they are structured correctly and can be injected into the application context. If a JAR does not comply, it will be rejected by the system, ensuring the integrity and stability of the core server.

#### Faydaları / Benefits:
- **Modülerlik / Modularity:** Özellikler modüller (JAR'lar) olarak ayrılır, bu da uygulamanın bakımını, güncellenmesini ve genişletilmesini kolaylaştırır.  
  Features are separated into modules (JARs), making it easier to maintain, update, and extend the application.
- **Ölçeklenebilirlik / Scalability:** Yeni işlevler, ana uygulamada değişiklik yapmadan sadece yeni eklentiler oluşturarak eklenebilir.  
  New functionalities can be added by simply creating new add-ons, without modifying the core application.
- **Hafif Ekosistem / Lightweight Ecosystem:** Sadece gerekli modüller yüklenir, bu da sistemin toplam hafıza ve kaynak kullanımını azaltır.  
  Only the necessary modules are loaded, reducing the overall memory and resource footprint of the system.
