<?php
$id_prefix = isset($_GET['id_prefix']) ? $_GET['id_prefix'] : "";
$lat = isset($_GET['latitude_value']) && !empty($_GET['latitude_value']) && $_GET['latitude_value'] > 0 ? $_GET['latitude_value'] : 25.6006638;
$lng = isset($_GET['longitude_value']) && !empty($_GET['longitude_value']) && $_GET['longitude_value'] > 0 ? $_GET['longitude_value'] : 49.5641327;
?>
<script>
var lat = <?php echo $lat; ?>;
var lng = <?php echo $lng; ?>;
if (typeof google === "undefined") {
	var s = document.createElement("script");
  s.setAttribute("async", "");
  s.setAttribute("defer", "");
  s.setAttribute("src", "https://maps.googleapis.com/maps/api/js?key=AIzaSyC6Wb-aax4wJFdacO55x8g35fez-__qNJE&callback=initialize");
  document.body.appendChild(s);
} else {
	initialize();
}
</script>
<div class="map-wrapper">
	<div id="map"></div>
	<input type="text" name="picker_address_name" readonly>
	<input type="hidden" name="picker_address_lat">
	<input type="hidden" name="picker_address_lng">
</div>
<script>
function initialize() {
	var precision = 10000000;
	var mapDiv = document.getElementById('map');
	var map = new google.maps.Map(mapDiv, {
		center: new google.maps.LatLng(lat, lng),
		zoom: 15,
		mapTypeId: google.maps.MapTypeId.ROADMAP,
		disableDefaultUI: true,
		clickableIcons: false,
		draggableCursor: "crosshair"
	});
	var marker_main = {
		url: "marker_main.png",
		size: new google.maps.Size(20, 20),
		origin: new google.maps.Point(0, 0),
		anchor: new google.maps.Point(10, 10),
		scaledSize: new google.maps.Size(20, 20)
	};
	var marker = new google.maps.Marker({
		position: {lat: lat, lng: lng},
		map: map
	});
	google.maps.event.addListener(map, 'click', function(e) {
		marker.setPosition(e.latLng);
    $("input[name=picker_address_lat]").val(e.latLng.lat());
    $("input[name=picker_address_lng]").val(e.latLng.lng());
		var url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + e.latLng.lat() + "," + e.latLng.lng() + "&sensor=false";
		$.getJSON(url, function (data) {
		var best = 0;
    for(var i=0;i<data.results.length;i++) {
        var ad = data.results[i].formatted_address;
        if (ad.length > best) {
        	best = ad.length;
        	$("input[name=picker_address_name]").val(ad);
        }
		  }
		});
	});
}
</script>
