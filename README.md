# Sphinx Kiosk - Setup & Usage Guide

## Table of Contents

- [Project Description](#project-description)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [Database](#database)
    - [Main Database Tables](#main-database-tables)
    - [Database Purpose](#database-purpose)
- [Admin Panel](#admin-panel)
    - [Accessing Admin Panel](#accessing-admin-panel)
    - [Admin Dashboard](#admin-dashboard)
    - [Adding New Menu Items](#adding-new-menu-items)
    - [Managing Menu Items](#managing-menu-items)
    - [Editing Menu Items](#editing-menu-items)
    - [Managing Categories](#managing-categories)
- [Customer Experience](#customer-experience)
    - [Starting an Order](#starting-an-order)
    - [Choosing Items](#choosing-items)
    - [Customizing Items](#customizing-items)
    - [Cart and Payment](#cart-and-payment)
    - [Order Completion](#order-completion)
- [Implemented Features](#implemented-features)
    - [Customer Features](#customer-features)
    - [Admin Features](#admin-features)
    - [Database Features](#database-features)
    - [UI and Design Features](#ui-and-design-features)
- [Design and Architecture](#design-and-architecture)
    - [Main Customer Screens](#main-customer-screens)
    - [Main Admin Screens](#main-admin-screens)
    - [Shared Styling](#shared-styling)
- [Project Structure](#project-structure)
- [Testing and Validation](#testing-and-validation)
- [Additional Notes](#additional-notes)
- [Development Process](#development-process)
- [Team](#team)

---

## Project Description

Sphinx Kiosk is a JavaFX self-service food ordering kiosk application. The system is designed for a fast-food restaurant environment where customers can place orders using a digital kiosk.

The customer can start an order, choose whether the order is for eat-in or takeaway, browse menu categories, view item details, customize selected items, add products to the cart, complete payment, and receive an order number.

The application also includes an admin section where staff can manage menu items, categories, prices, availability, and product information. The system uses an SQLite database to store menu data and admin changes.

---

## Prerequisites

Before running the project, make sure you have:

- IntelliJ IDEA installed
- Java installed
- Gradle available through the project
- The project cloned from GitLab

---

## Installation & Setup

1. Clone the repository from GitLab.

   ```bash
   git clone <repository-url>
   ```

2. Open the project in **IntelliJ IDEA**.

3. Make sure the project is opened from the correct root folder where the Gradle files are located.

4. Wait for IntelliJ to load and sync Gradle.

5. If Gradle does not load automatically, refresh the Gradle project from the Gradle panel.

---

## Running the Application

The application can be run in different ways.

### Option 1: IntelliJ Gradle Menu

1. Open the Gradle panel on the right side of IntelliJ.
2. Go to:

   ```text
   app → Tasks → application → run
   ```

3. Double-click `run`.

### Option 2: IntelliJ Run Button

1. Open the main application file.
2. Click the green run button if IntelliJ detects the application configuration.

### Option 3: Terminal

On Windows:

```bash
gradlew run
```

On macOS/Linux:

```bash
./gradlew run
```

When the application starts, the welcome screen should appear.

---

## Database

The project uses **SQLite** as the database.

Database file:

```text
kiosk.db
```

The database is initialized through:

```text
DatabaseInitializer.java
```

The main database-related classes are located in:

```text
app/src/main/java/se/lnu/database
```

### Main Database Tables

The main database tables include:

- `Category`
- `MenuItem`
- `ExtraOption`
- `RemovableIngredient`
- `MenuItemExtraOption`
- `MenuItemRemovableIngredient`
- `ComboChoiceGroup`
- `ComboChoiceOption`

### Database Purpose

The database stores:

- Menu categories
- Menu items
- Item descriptions
- Item prices
- Item availability
- Optional extras
- Removable ingredients
- Combo choices
- Category connections
- Admin menu changes

Default menu data is inserted when the `MenuItem` table is empty. Admin-added menu items are saved to the database and should remain after restarting the application.

---

## Admin Panel

The admin panel is used by staff to manage menu-related data.

### Accessing Admin Panel

1. Open the application.
2. From the welcome screen, access the admin login.
3. Enter the admin credentials.
4. After successful login, the admin dashboard is displayed.

The admin dashboard allows staff to manage menu items, categories, prices, and item availability.

---

### Admin Dashboard

The admin dashboard includes access to:

- Add new menu items
- Manage existing menu items
- Edit menu items
- Manage categories
- Log out and return to the welcome screen

---

### Adding New Menu Items

1. From the admin dashboard, click **Add New Menu Item**.
2. Enter the required product details:
    - Item name
    - Description
    - Price
    - Category
3. Click **Save Menu Item**.
4. The new item is saved to the database.
5. The item appears on the customer side under the selected category.

This feature allows staff to add new products without manually editing the database.

---

### Managing Menu Items

1. From the admin dashboard, click **Manage Menu Items**.
2. Select a category.
3. View the menu items in that category.
4. Delete products that should no longer be sold, if needed.

This screen is used for managing existing products in the menu.

---

### Editing Menu Items

1. From the admin dashboard, click **Edit Menu Items**.
2. Select an existing menu item.
3. Edit item information such as:
    - Name
    - Description
    - Price
    - Availability
4. Save the changes.

Availability controls whether the item appears on the customer side. If an item is marked unavailable, it should not be shown to customers.

---

### Managing Categories

1. From the admin dashboard, click **Manage Categories**.
2. View existing categories.
3. Add a new category if needed.
4. Delete categories if needed.

Categories are used to organize menu items on the customer side.

Examples of categories:

- Burgers
- Drinks
- Sides
- Desserts
- Combos

---

## Customer Experience

### Starting an Order

1. Start the application.
2. Click **Start Order** on the welcome screen.
3. Choose either **Eat-in** or **Takeaway**.

The selected order type is stored and used during the ordering flow.

---

### Choosing Items

1. Select a menu category:
    - Burgers
    - Drinks
    - Sides
    - Desserts
    - Combos
2. Choose a menu item.
3. View the item details.

Each menu item includes information such as name, description, and price.

---

### Customizing Items

Depending on the selected item, customers may be able to:

- Add optional extras
- Remove ingredients
- Choose combo options

After customization, the item can be added to the cart.

---

### Cart and Payment

1. Open the cart.
2. Review selected items.
3. Adjust quantity if needed.
4. Check the total price.
5. Continue to payment.
6. Complete the order.

The cart keeps track of selected items, quantity, and total cost.

---

### Order Completion

After payment, the customer receives an order number.

The order number screen displays the order number briefly and then automatically returns to the welcome screen. This helps reset the kiosk for the next customer.

---

## Implemented Features

### Customer Features

- Welcome screen with kiosk-style design
- Eat-in or takeaway order type selection
- Menu category browsing
- Item details screen
- Optional extras for menu items
- Removable ingredients for menu items
- Combo meal selection
- Cart screen with quantity and total price updates
- Persistent cart button/flow across relevant screens
- Payment flow
- Order confirmation screen
- Order number screen
- Automatic return/reset after order completion

---

### Admin Features

- Admin login
- Admin dashboard
- Add new menu items
- Manage existing menu items
- Edit menu item name, description, price, and availability
- Manage categories
- Save menu and category changes to the database
- Database persistence so admin-added items can remain after restarting the app

---

### Database Features

- SQLite database integration
- Automatic database initialization
- Default menu data seeding
- Menu item storage
- Category storage
- Optional extras storage
- Removable ingredients storage
- Combo choice storage
- Admin-added menu item persistence
- Availability filtering for customer-side menu items

---

### UI and Design Features

- Kiosk-style customer interface
- Large buttons for easy interaction
- Warm orange/yellow visual theme
- Rounded cards and buttons
- Shared background style
- Consistent navigation buttons
- Separate customer and admin flows
- Improved first customer screens for a more professional kiosk appearance

---

## Design and Architecture

The application is structured using separate JavaFX screens. Each screen handles one part of the customer or admin flow.

The application follows a screen-based structure where navigation is handled by calling the `show(Stage stage)` method of different screen classes.

---

### Main Customer Screens

Examples of customer-side screens:

- `WelcomeScreen`
- `OrderTypeScreen`
- `CategoryScreen`
- `ItemDetailsScreen`
- `MealSelectionScreen`
- `CartScreen`
- `PaymentScreen`
- `OrderConfirmationScreen`
- `OrderNumberScreen`

---

### Main Admin Screens

Examples of admin-side screens:

- `AdminLoginScreen`
- `AdminDashboardScreen`
- `AdminAddMenuItemScreen`
- `AdminMenuItemsScreen`
- `CategoryAdminScreen`
- `itemEditorScreen`

---

### Shared Styling

The project uses a shared style helper class:

```text
ScreenStyle.java
```

This class helps keep the UI consistent across screens by providing shared styling for:

- Backgrounds
- Cards
- Buttons
- Navigation buttons

The UI follows a kiosk-style design with:

- Large buttons
- Simple navigation
- Clear customer flow
- Warm orange/yellow color theme
- Rounded cards and buttons
- Customer-friendly layout
- Separate admin and customer flows

---

## Project Structure

```text
app/src/main/java/org/example
```

Contains the main application entry point used to start the JavaFX application.

```text
app/src/main/java/se/lnu
```

Contains the main customer-side screens, models, cart logic, application flow, and shared styling.

```text
app/src/main/java/se/lnu/admin
```

Contains admin-related screens such as login, dashboard, add menu item screen, menu item management, item editing, and category management.

```text
app/src/main/java/se/lnu/database
```

Contains database connection, database helper methods, and database initialization logic.

```text
app/src/main/resources
```

Contains resources used by the application, such as images or other UI assets.

---

## Testing and Validation

The application was tested manually during the sprint process.

Main tested flows include:

- Starting a customer order
- Selecting order type
- Browsing categories
- Opening item details
- Adding optional extras
- Removing ingredients
- Choosing combo options
- Adding items to cart
- Updating cart quantity
- Checking total price updates
- Completing payment flow
- Showing order number
- Returning to the welcome screen after order completion
- Admin login
- Adding a new menu item
- Checking that newly added items appear on the customer side
- Editing item details and availability
- Managing categories
- Checking database persistence after restarting the application

---

## Additional Notes

- The application is designed as a course project and prototype.
- Admin credentials are handled inside the project code for testing purposes.
- The database is initialized automatically when the application runs.
- Default data is inserted when the `MenuItem` table is empty.
- Admin-added items should remain saved after restarting the app.
- Images are generated or selected based on item names where applicable.
- Availability controls whether a menu item is visible to customers.

---

## Development Process

This project was developed as a group project using an agile/scrum workflow. The team worked through multiple sprints and improved the application step by step.

The development process included:

- Sprint planning
- User story selection
- Branch-based development
- GitLab merge requests
- UI improvements
- Database integration
- Admin feature development
- Manual testing
- Final documentation

---

## Team

Sphinx Group