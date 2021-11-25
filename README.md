## Getting Started

Welcome to my java_shop

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
  - `com.invoice`: class of product in the invoice
  - `com.product`: class of product
  - `com.user`: class of user
  - `connector`: connect to database, retrieve data, update data
  - `ui.controller`: controllers of fxml files
  - `ui.view`: fxml files
  - `main`: main
- `resource`: image, ...

## Dependency Management

In setting.json, at `java.project.referencedLibraries` tag, add all absolute path of all javafx libraries to reference to them  
In launch.json, add new tag below `mainClass: "main.MainApp` tag: `"vmArgs": "--module-path absolute_path_to_javafx_lib --add-modules javafx.controls,javafx.fxml"`
