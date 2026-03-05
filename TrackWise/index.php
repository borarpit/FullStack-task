<?php
session_start();

if(!isset($_SESSION['user_id'])){
    header("Location: login.php");
    exit();
}

include 'connect.php';

$user_id = $_SESSION['user_id'];

// Total Expense
$totalQuery = "SELECT SUM(amount) as total FROM expenses WHERE user_id='$user_id'";
$totalResult = mysqli_query($conn, $totalQuery);
$totalRow = mysqli_fetch_assoc($totalResult);
$total = $totalRow['total'] ? $totalRow['total'] : 0;

// Total Entries
$countQuery = "SELECT COUNT(*) as count FROM expenses WHERE user_id='$user_id'";
$countResult = mysqli_query($conn, $countQuery);
$countRow = mysqli_fetch_assoc($countResult);
$count = $countRow['count'] ? $countRow['count'] : 0;
?>

<!DOCTYPE html>
<html>
<head>
<title>TrackWise Dashboard</title>
<link rel="stylesheet" href="style.css">
</head>

<body>

<div class="container">

<div class="sidebar">
    <h2>💰 TrackWise</h2>
    <a href="index.php">Dashboard</a>
    <a href="viewExpense.php">View Expenses</a>
    <a href="analytics.php">Analytics</a>
    <a href="logout.php">Logout</a>
</div>

<div class="main">

<h1>Welcome, <?php echo htmlspecialchars($_SESSION['username']); ?> 👋</h1>

<div class="dashboard-cards">
    <div class="card">
        <h3>Total Expense</h3>
        <p>₹<?php echo number_format($total,2); ?></p>
    </div>

    <div class="card">
        <h3>Total Entries</h3>
        <p><?php echo $count; ?></p>
    </div>
</div>

<div class="form-card">
    <h2>Add New Expense</h2>
    <form action="addExpense.php" method="post">
        <input type="text" name="name" placeholder="Expense Name" required>
        <input type="number" name="amount" placeholder="Amount" step="0.01" required>
        <input type="text" name="category" placeholder="Category" required>
        <input type="date" name="date" required>
        <button type="submit">Add Expense</button>
    </form>
</div>

</div>
</div>

</body>
</html>