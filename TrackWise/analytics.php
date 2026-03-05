<?php
include 'connect.php';

$query = "SELECT category, SUM(amount) as total FROM expenses GROUP BY category";
$result = mysqli_query($conn, $query);

$categories = [];
$totals = [];

while($row = mysqli_fetch_assoc($result)){
    $categories[] = $row['category'];
    $totals[] = $row['total'];
}
?>

<!DOCTYPE html>
<html>
<head>
<title>Analytics</title>
<link rel="stylesheet" href="style.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<div class="container">

<div class="sidebar">
    <h2>💰 TrackWise</h2>
    <a href="index.php">Dashboard</a>
    <a href="viewExpense.php">View Expenses</a>
    <a href="analytics.php">Analytics</a>
</div>

<div class="main">
    <h1>Expense Analytics</h1>
    <canvas id="myChart"></canvas>
</div>

</div>

<script>
new Chart(document.getElementById('myChart'), {
type: 'bar',
data: {
labels: <?php echo json_encode($categories); ?>,
datasets: [{
data: <?php echo json_encode($totals); ?>,
backgroundColor: [
'#667eea',
'#764ba2',
'#f093fb',
'#f5576c',
'#4facfe',
'#43e97b'
]
}]
},
options: {
responsive: true,
plugins: {
legend: { display: false }
}
}
});
</script>

</body>
</html>