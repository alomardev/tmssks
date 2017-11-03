$(function() {
	setupComponents();

	$("#editor-form input, #editor-form select, input[type=submit], #editor-form textarea").attr("disabled", "");
	$("#editor-form-submit").click(function(e) {
		// check for required inputs
		$("*[required]").each(function() {
			if ($(this).val().length < 1) {
				$(this).toggleClass("error", true);
			}
		});

		if ($(".error").length > 0) {
			return;
		}

		updateStatus("Saving...");
		$.ajax({
			type: "POST",
			url: $("#editor-form").attr("action"),
			data: $("#editor-form").serialize(),
			success: function(response) {
				response = JSON.parse(response);
				updateStatus(response.message, true);
				loadData();
			}
		});
	});

	loadData();
});

/*
required
input-prop-username
input-prop-digits
input-prop-phone
input-prop-email
input-prop-minlength: value

input-prop-address: text_holder|latitude_holder|longitude_holder
input-prop-combo: link|key|label|default

input-prop-readable-new
*/

function setupComponents() {
	// Ajaxed ComboBox
	var comboBoxes = $("select[input-prop-combo]");
	for (var i = 0; i < comboBoxes.length; i++) {
		var e = $(comboBoxes[i]);
		segs = e.attr("input-prop-combo").split("|");
		e.append("<option value=''>" + segs[3] + "</option>");
		var context = {element: e, segs: segs};
		$.ajax({
			type: "POST",
			url: segs[0],
			context: context,
			success: function(response) {
				cdata = JSON.parse(response);
				var cxt = this;
				for (var i = 0; i < cdata.length; i++) {
					cxt.element.append("<option value='" + cdata[i][cxt.segs[1]] + "'>" + cdata[i][cxt.segs[2]] + "</option>");
				}
			}
		})
	}

	// Address
	var addressInputs = $("input[input-prop-address]");
	for (var i = 0; i < addressInputs.length; i++) {
		var e = $(addressInputs[i]);
		var addressParams = e.attr("input-prop-address").split("|");
		var addressName = $("input[name=" + addressParams[0] + "]");
		var addressLat = $("input[name=" + addressParams[1] + "]");
		var addressLng = $("input[name=" + addressParams[2] + "]");
		e.on("click", function() {
			$.ajax({
				type: "GET",
				data: "latitude_value=" + addressLat.val() + "&longitude_value=" + addressLng.val(),
				url: "php/ui.addresspicker.php",
				success: function(response) {
					showMessageBox("Pick an Address", response, "Submit", "Cancel", function(box) {
						addressName.val($("input[name=picker_address_name]").val());
						addressLat.val($("input[name=picker_address_lat]").val());
						addressLng.val($("input[name=picker_address_lng]").val());
					});
				}
			})
		});
	}

	// Realtime validation
	$("*[input-prop-minlength]").each(function() {
		var e = $(this);
		e.bind("propertychange change click keyup input paste", function() {
			e.toggleClass("error", e.val().length < Number(e.attr("input-prop-minlength")));
		});
	});

	$("*[input-prop-digits]").each(function() {
		var e = $(this);
		e.bind("propertychange change click keyup input paste", function() {
			var regex = /^\d*$/;
			e.toggleClass("error", !regex.test(e.val()));
		});
	});

	$("*[input-prop-phone]").each(function() {
		var e = $(this);
		e.bind("propertychange change click keyup input paste", function() {
			var regex = /(^$|^(05|\+9665|009665)\d{8}$)/;
			e.toggleClass("error", !regex.test(e.val()));
		});
	});

	$("*[input-prop-email]").each(function() {
		var e = $(this);
		e.bind("propertychange change click keyup input paste", function() {
			var regex = /(^$|^\S+@\S+\.\S+$)/;
			e.toggleClass("error", !regex.test(e.val()));
		});
	});
}

var data = [];
var selectedIndex = -1;
var newRecord = false;

function loadData() {
	$.ajax({
			type: "POST",
			url: loadUrl,
			success: function(response) {
				$("#editor-list").empty();
				data = JSON.parse(response);
				for (var i = 0; i < data.length; i++) {
					data[i].index = i;
					data[i].selected = false;
					$("#editor-list").append("<div data-index='" + i + "' class='list-item' onclick='onEditorListItemClick(this)'>" + data[i][listItemProp] + "</div>");
				}
				$("#entities-count").text(data.length);
				setSelected(newRecord ? data.length - 1 : selectedIndex);
			}
		});
}

function setSelected(index, newItem = false) {

	$(".error").toggleClass("error", false);

	selectedIndex = index;
	newRecord = newItem;
	if (index > -1) {
		var listItems = $("#editor-list .list-item");
		for (var i = 0; i < listItems.length; i++) {
			$(listItems[i]).toggleClass("selected", $(listItems[i]).data("index") == index);
		}
	} else {
		$("#editor-list .list-item").toggleClass("selected", false);
	}
	if (index > -1 || newItem) {
		$("#editor-form input, #editor-form select, input[type=submit], #editor-form textarea").removeAttr("disabled");
	} else {
		$("#editor-form input, input[type=submit], #editor-form textarea").attr("disabled", "");
	}
	$("#new-attention").css("display", newItem ? "inline" : "none");
	if (newItem) {
		$("#editor input[input-prop-readable-new], #editor-form textarea").removeAttr("readonly");
	} else {
		$("#editor input[input-prop-readable-new], #editor-form textarea").attr("readonly", "");
	}
	updateFields(index);
}

function onEditorListItemClick(source) {
	setSelected(source == null ? -1 : $(source).data("index"));
}

function updateFields(index) {
	$("#editor-form input").val("");
	$("#editor-form select").val("");
	if (index > -1) {
		for (var i in data[index]) {
			if (data[index][i] != undefined) {
				$("#editor-form input[name=" + i + "]").val(data[index][i]);
				$("#editor-form select[name=" + i + "]").val(data[index][i]);
			}
		}
	}
}

function onAddItem() {
	setSelected(-1, true);
}

function onRemoveItem() {
	var element = $("#editor-list .selected");
	var id = data[selectedIndex].id === undefined ? data[selectedIndex].nid : data[selectedIndex].id; // hack
	if (element.length > 0) {
		if (confirm("Are you sure?")) {
			$.ajax({
				type: "POST",
				url: deleteUrl,
				data: "id=" + id,
				success: function(response) {
					response = JSON.parse(response);
					updateStatus(response.message, true);
					loadData();
				}
			});
		}
	}
}

function updateStatus(message, removeAfterWhile = false) {
	$("#editor-form-status").text(message);
	if (removeAfterWhile) {
		setTimeout(function() {
			$("#editor-form-status").text("");
		}, 1000 + (message.length * 80));
	}
}

function showMessageBox(title, body, positive = "", negative = "", positiveCallback = null, negativeCallback = null) {
	var box = $(
			"<div class='message-box-overlay'><div id='msgbox' class='message-box'>" +
			"<div class='message-box-header'>" + title + "</div>" +
			"<div class='message-box-body'>" + body + "</div>" +
			(negative.length > 0 || positive.length > 0 ? "<div class='message-box-footer'>" : "") +
			(negative.length > 0 ? "<a class='button message-box-btn-negative'>" + negative + "</a>" : "") +
			(positive.length > 0 ? "<a class='button message-box-btn-positive'>" + positive + "</a>" : "") +
			(negative.length > 0 || positive.length > 0 ? "</div>" : "") +
			"</div></div>"
		);
	box.dismissed = false;
	$("body").append(box);

	var dismiss = function() {
		if (!box.dismissed) {
			box.remove();
			box.dismissed = true;
		}
	};

	$(".message-box-btn-negative").on("click", function() {
		if (negativeCallback != null)
			negativeCallback(box[0]);
		dismiss();
	});
	$(".message-box-btn-positive").on("click", function() {
		if (positiveCallback != null)
			positiveCallback(box[0]);
		dismiss();
	});
}
