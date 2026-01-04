# Resume-as-Code Engine

The project is a "Resume-as-Code" application built with Spring Boot. It allows users to define their professional identity in a structured YAML format and programmatically generate high-quality, professional PDFs using customizable themes.

## 🚀 Project Overview
The project is designed with a modular, scalable architecture, focusing on performance, resilience, and API integrity. It transitions traditional resume building into a developer-centric workflow.

### Core Tech Stack
* **Framework:** Spring Boot 3.5.9


* **Language:** Java 21


* **Build Tool:** Maven


* **Parsing:** Jackson YAML Dataformat


* **Templating:** Thymeleaf


* **PDF Engine:** OpenHTMLToPDF (based on Apache PDFBox)


* **Boilerplate:** Project Lombok
---

## 🏗️ System Architecture

### 1. API Integrity & Exception Handling
Implemented a centralized error-handling strategy to ensure a robust contract with the frontend.

* **Structured API Errors:** All exceptions return a standardized JSON response containing a timestamp, error code, and field-level validation details.

* **Validation:** Utilizes `jakarta.validation` to enforce strict schema requirements on the YAML input before processing.

* **JSON Serialization:** Customized Jackson configuration to handle Java 8 Date/Time types (`LocalDateTime`) and global formatting.


### 2. High-Performance Resource Management
Optimized for production speed by minimizing Disk I/O and handling external assets safely.

* **Font Memory Cache:** Fonts (Source Sans 3 and Roboto) are pre-loaded into a `ConcurrentHashMap` at application startup, reducing PDF generation time significantly.

* **Resilient Image Fetching:** A dedicated `ResourceService` fetches remote profile photos and converts them to Base64 with built-in error handling to prevent rendering crashes if a URL is broken.

* **Data Sanitization:** Implemented recursive sanitization to handle XML-sensitive characters (like `&`) across all nested YAML data structures.


### 3. Flexible Theme Engine
The rendering engine is **Schema-Agnostic**, allowing for maximum user flexibility.

* **Dynamic Layouts:** Users can control the order of sections and their placement (Main vs. Sidebar) via the `layout` DTO in the YAML.

* **Polymorphic Rendering:** Templates automatically adapt their visual style based on data shape—supporting multi-line paragraphs, simple bullet lists, and complex nested maps.

* **Deep Customization:** Supports user-defined colors for names, section titles, entry headers, and subtitles.

* **Rich Formatting:** Enabled `th:utext` support, allowing users to include standard HTML tags like `<b>` (bold) and `<i>` (italic) directly in their YAML content.

---

## 📂 Project Structure

```text
src/main/java/com/praj/rendercv/
├── config/             # Jackson & Spring configurations
├── controller/         # REST Endpoints
├── dto/                # Data Transfer Objects & Schema definitions
├── exception/          # Global Exception Handler & Custom Exceptions
├── service/            # Business Logic & Asset Management
└── util/               # Helper utilities
src/main/resources/
├── fonts/              # TTF Font resources (Roboto, Source Sans 3)
└── templates/themes/   # Thymeleaf HTML Themes (Classic, Modern, Sidebar)
```
---

## 🔌 API Endpoints

### 1. Test Parse
`POST /api/v1/resume/test-parse`

* **Input:** Raw YAML string.

* **Output:** Returns the parsed `ResumeRequest` object or detailed validation errors.


### 2. Render Resume
`POST /api/v1/resume/render`

* **Input:** Raw YAML string.

* **Output:** Returns a downloadable/viewable PDF file with `Content-Disposition: attachment`.

---

## 🎨 Available Themes

| Theme       | Type                  | Key Features                                                                |
|-------------|-----------------------|-----------------------------------------------------------------------------|
| **Classic**     | Single Column         | Professional, academic style with right-aligned dates.                      |
| **Modern**      | Two-Column            | Clean, vertical split with customizable header and entry colors.            |
| **Sidebar**     | Two-Column            | Fixed left sidebar with a distinct background for contact and skills.       |

---

## 🛠️ Getting Started

### Prerequisites
* JDK 21 or higher
* Maven 3.x

### Installation
1. Clone the repository.

```bash
git clone <repository-url>
```

2. Install dependencies:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```
---
