# ParsePoint: A Robust Fixed-Length Flat File Parser
### By Muhammed Nafees
## Introduction

ParsePoint streamlines the processing of fixed-length flat files by enabling efficient parsing, secure archiving, and organized storage of parsed data. It empowers you to:

* Effortlessly parse flat files based on custom specifications.
* Store parsed records in a MongoDB database for centralized access.
* Archive original files in block/object storage for reliable preservation.
* View parsed data through intuitive tabular and filterable views.

## Benefits

* **Enhanced Efficiency:** Streamline flat file processing and data management.
* **Simplified Archival:** Securely archive original flat files for future reference.
* **Organized Storage:** Maintain parsed data in a readily accessible database.
* **Clear Visibility:** Gain immediate insights into parsed records with informative views.
* **User-Friendly Interaction:** Interact with ParsePoint using a modern web interface.

## Technology Stack

* Backend: Spring Boot ([https://start.spring.io/](https://start.spring.io/))
* Database: MongoDB ([https://www.mongodb.com/](https://www.mongodb.com/))
* Frontend: Angular ([https://angular.io/](https://angular.io/))
* Communication: REST API ([https://www.restapitutorial.com/](https://www.restapitutorial.com/))

## Web Pages

* **Login & Registration:** Securely register for a user account or log in to an existing one.
* **File Parser:**
  * Upload a flat file, its corresponding spec file, and specify the record type.
  * Parse the file by clicking the "Parse File" button.
  * View the parsed records displayed in clear tabular format, allowing you to process multiple records simultaneously.
* **View Records:**
  * Select filter criteria to refine the list of displayed records.
  * View all previously parsed records belonging to your user account or apply specific filters.
