<div class="input-group">
	<label for="nid">National ID *</label>
	<input name="nid" type="text" placeholder="National ID" readonly required input-prop-readable-new input-prop-digits>
</div>
<div class="input-group">
	<label for="name">Name *</label>
	<input name="name" type="text" placeholder="Full name" required>
</div>
<div class="input-group">
	<label for="address_title">Address</label>
	<input name="address_title" type="text" placeholder="Home Address" input-prop-address="address_title|address_latitude|address_longitude">
	<input name="address_latitude" type="hidden">
	<input name="address_longitude" type="hidden">
</div>
<div class="row">
	<div class="col input-group">
		<label for="level">Level</label>
		<input name="level" type="text" placeholder="Level" >
	</div>
	<div class="col input-group">
		<label for="trans_id">Select Transportation</label>
		<select name="trans_id" input-prop-combo="./api/get-trans.php|id|num_plate|---">
		</select>
	</div>
</div>
<div class="row">
	<div class="col input-group">
		<label for="parent1">Select Parent</label>
		<select name="parent1" input-prop-combo="./api/get-users.php?role=2|id|name|---">
		</select>
	</div>
	<div class="col input-group">
		<label for="parent2">Select Another Parent</label>
		<select name="parent2" input-prop-combo="./api/get-users.php?role=2|id|name|---">
		</select>
	</div>
</div>
