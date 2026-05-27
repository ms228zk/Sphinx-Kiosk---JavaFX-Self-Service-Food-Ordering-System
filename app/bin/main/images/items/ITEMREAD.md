## How to Add Item Images

### Step 1: Create Image Files

1. Create image files for each menu item
2. Place them in: `/src/main/resources/images/items/`
3. **Filename Format:** `item_name_in_lowercase.png`

**Examples:**

```
BBQ Smash Burger       → bbq_smash_burger.png
Double Beef Patty      → double_beef_patty.png
Classic Cheeseburger   → classic_cheeseburger.png
Fresh Orange Juice     → fresh_orange_juice.png
French Fries           → french_fries.png
```

### Step 2: Image Specifications

- **Format:** PNG (recommended) or JPG
- **Size:** Recommended 300x300 pixels (will be scaled automatically)
- **Aspect Ratio:** Can be any (images preserve aspect ratio)
- **Quality:** Use good quality images

---

## Image Display Locations

### 1. Category/Items Screen (CategoryScreen.java)

- **Size:** 180x140 pixels
- **Location:** Above item name in card
- **Background:** Light gray box with rounded corners
- **Fallback:** Shows gray placeholder if image missing

### 2. Item Details Screen (ItemDetailsScreen.java)

- **Size:** 320x320 pixels
- **Location:** Left side of detail view
- **Fallback:** Shows gray placeholder if image missing

### 3. Cart Screen (CartScreen.java)

- **Size:** 100x80 pixels
- **Location:** Between item name and quantity controls
- **Background:** Light gray box with rounded corners
- **Fallback:** Shows gray placeholder if image missing
- **Layout:** Displays in vertical item details section
