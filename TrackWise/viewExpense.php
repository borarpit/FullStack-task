<?php
include 'connect.php';

$sql = "SELECT * FROM expenses";
$result = mysqli_query($conn, $sql);

$total = 0;
?>

<!DOCTYPE html>
<html>
<head>
<title>View Expenses</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<h2>📋 Expense List</h2>

<table border="1" cellpadding="10" style="margin:auto; background:white; color:black;">
<tr>
<th>Name</th>
<th>Amount</th>
<th>Category</th>
<th>Date</th>
<th>Action</th>
</tr>

<?php
while($row = mysqli_fetch_assoc($result)) {
$total += $row['amount'];
?>

<tr>
<td><?php echo $row['name']; ?></td>
<td>₹<?php echo $row['amount']; ?></td>
<td><?php echo $row['category']; ?></td>
<td><?php echo $row['date']; ?></td>
<td>
<a href="delete.php?id=<?php echo $row['id']; ?>">Delete</a>
</td>
</tr>

<?php } ?>

</table>

<h3>Total Expense: ₹<?php echo $total; ?></h3>

<br>
<a href="index.php">⬅ Back to Dashboard</a>
<br><br>
<a href="analytics.php">📊 View Analytics</a>

</body>
</html>