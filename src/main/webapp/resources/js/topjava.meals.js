var meals;

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    meals = {
        ajaxUrl: "profile/meals/",
        datatableApi: $("#dataMealTable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Update",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    };
    makeEditable();
});

var form;
function makeEditable() {
    form = $('#detailsMealForm');

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function addMeal() {
    form.find(":input").val("");
    $("#editMealRow").modal();
}

function saveMeal() {
    $.ajax({
        type: "POST",
        url: meals.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editMealRow").modal("hide");
        updateMealTable();
        successNoty("Saved");
    });
}

function deleteMealRow(id) {
    if (confirm('Are you sure?')) {
        $.ajax({
            url: meals.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            updateMealTable();
            successNoty("Deleted");
        });
    }
}

function updateMealTable() {
    $.get(meals.ajaxUrl, function (data) {
        meals.datatableApi.clear().rows.add(data).draw();
    });
}

var failedNote;
function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}
function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}
function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}