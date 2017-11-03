<?php
require_once "../php/app.php";
openSession();

$trans = array();
$trans['id'] = $_POST['id'];
$trans['num_plate'] = $_POST['num_plate'];
$trans['driver_name'] = $_POST['driver_name'];
$trans['driver_phone'] = $_POST['driver_phone'];

echo saveTrans($trans);
?>
