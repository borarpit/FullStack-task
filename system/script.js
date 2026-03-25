// --- DATABASE INITIALIZATION (IndexedDB) ---

let db;
const DB_NAME = "InventoryDB";
const DB_VERSION = 1;

// Open Database
const request = indexedDB.open(DB_NAME, DB_VERSION);

// Create Relational Tables (Object Stores)
request.onupgradeneeded = function(event) {
    db = event.target.result;

    // 1. Categories Table
    if (!db.objectStoreNames.contains("categories")) {
        const catStore = db.createObjectStore("categories", { keyPath: "id", autoIncrement: true });
        catStore.createIndex("name", "name", { unique: false });
    }

    // 2. Products Table (Links to Category via category_id)
    if (!db.objectStoreNames.contains("products")) {
        const prodStore = db.createObjectStore("products", { keyPath: "id", autoIncrement: true });
        prodStore.createIndex("category_id", "category_id", { unique: false });
        prodStore.createIndex("sku", "sku", { unique: true });
    }
};

request.onsuccess = function(event) {
    db = event.target.result;
    console.log("Database connected successfully.");
    loadCategories(); // Load dropdown
    loadProducts();   // Load table
};

request.onerror = function(event) {
    console.error("Database error:", event.target.errorCode);
};


// --- CATEGORY FUNCTIONS ---

function addCategory() {
    const nameInput = document.getElementById("newCategory");
    const name = nameInput.value.trim();

    if (!name) return alert("Please enter a category name");

    const transaction = db.transaction(["categories"], "readwrite");
    const store = transaction.objectStore("categories");
    
    // Check if category exists
    const checkIndex = store.index("name");
    const checkRequest = checkIndex.get(name);
    
    checkRequest.onsuccess = function() {
        if (checkRequest.result) {
            alert("Category already exists!");
        } else {
            store.add({ name: name });
            nameInput.value = "";
            loadCategories();
            alert("Category Added!");
        }
    };
}

function loadCategories() {
    const transaction = db.transaction(["categories"], "readonly");
    const store = transaction.objectStore("categories");
    const request = store.getAll();

    request.onsuccess = function() {
        const select = document.getElementById("prodCat");
        select.innerHTML = "<option value=''>Select Category</option>";
        
        request.result.forEach(cat => {
            const option = document.createElement("option");
            option.value = cat.id;
            option.textContent = cat.name;
            select.appendChild(option);
        });
    };
}


// --- PRODUCT FUNCTIONS ---

function addProduct() {
    const name = document.getElementById("prodName").value.trim();
    const sku = document.getElementById("prodSku").value.trim();
    const catId = parseInt(document.getElementById("prodCat").value);
    const qty = parseInt(document.getElementById("prodQty").value);
    const price = parseFloat(document.getElementById("prodPrice").value);

    // Validation
    if (!name || !sku || !catId || isNaN(qty) || isNaN(price)) {
        return alert("Please fill in all fields correctly.");
    }

    const transaction = db.transaction(["products"], "readwrite");
    const store = transaction.objectStore("products");

    // Add Product Object (Relational link via category_id)
    store.add({
        name: name,
        sku: sku,
        category_id: catId,
        quantity: qty,
        price: price,
        created_at: new Date()
    });

    transaction.oncomplete = function() {
        // Clear inputs
        document.getElementById("prodName").value = "";
        document.getElementById("prodSku").value = "";
        document.getElementById("prodQty").value = "0";
        document.getElementById("prodPrice").value = "";
        
        loadProducts();
        alert("Product saved successfully!");
    };
}

function loadProducts() {
    const transaction = db.transaction(["categories", "products"], "readonly");
    const prodStore = transaction.objectStore("products");
    const catStore = transaction.objectStore("categories");
    
    const prodRequest = prodStore.getAll();

    prodRequest.onsuccess = function() {
        const products = prodRequest.result;
        const tbody = document.getElementById("inventoryTable");
        tbody.innerHTML = "";

        if (products.length === 0) {
            tbody.innerHTML = "<tr><td colspan='8' style='text-align:center'>No products found</td></tr>";
            return;
        }

        products.forEach(p => {
            // Fetch Category Name (JOIN Logic)
            const catRequest = catStore.get(p.category_id);
            
            catRequest.onsuccess = function() {
                const catName = catRequest.result ? catRequest.result.name : "Uncategorized";
                
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.sku}</td>
                    <td>${catName}</td>
                    <td><strong>${p.quantity}</strong></td>
                    <td>$${p.price.toFixed(2)}</td>
                    <td>
                        <button class="btn btn-success stock-btn" onclick="updateStock(${p.id}, 'in')">+</button>
                        <button class="btn btn-danger stock-btn" onclick="updateStock(${p.id}, 'out')">-</button>
                    </td>
                    <td>
                        <button class="btn btn-warning stock-btn" onclick="deleteProduct(${p.id})">X</button>
                    </td>
                `;
                tbody.appendChild(tr);
            };
        });
    };
}

// --- STOCK MANAGEMENT ---

function updateStock(id, type) {
    const transaction = db.transaction(["products"], "readwrite");
    const store = transaction.objectStore("products");
    
    const request = store.get(id);

    request.onsuccess = function() {
        const data = request.result;
        let newQty = data.quantity;

        if (type === 'in') {
            newQty++;
        } else {
            if (newQty > 0) {
                newQty--;
            } else {
                alert("Stock is already 0!");
                return;
            }
        }

        data.quantity = newQty;
        store.put(data); // Update database
        
        loadProducts(); // Refresh UI
    };
}

function deleteProduct(id) {
    if(!confirm("Are you sure you want to delete this item?")) return;

    const transaction = db.transaction(["products"], "readwrite");
    const store = transaction.objectStore("products");
    store.delete(id);

    transaction.oncomplete = function() {
        loadProducts();
    };
}