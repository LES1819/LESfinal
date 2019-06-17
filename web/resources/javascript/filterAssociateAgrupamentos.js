/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    // Setup - add a text input to each footer cell
    $('#example thead tr').clone(true).appendTo( '#example thead' );


    $('#example thead tr:eq(1) th').each( function (i) {
     
     if(i >= 1 && i <= 4){   
        $(this).html( '<input type="text"/>' );
 
        $( 'input', this ).on( 'keyup change', function () {
            if ( table.column(i).search() !== this.value ) {
                table.column(i).search( this.value ).draw();
            }
        } );
        }
    } );
    $('#example thead tr:eq(1) th:eq(5)').html("");
 
    var table = $('#example').DataTable( {
        orderCellsTop: true,
        fixedHeader: true,
        order: [],
        "language": {
        "search": "Procurar: ",
        "emptyTable":     "A tabela de agrupamentos está vazia",
        "info": "A mostrar entre _START_ a _END_ no total de _TOTAL_ agrupamentos",
        "zeroRecords":    "Não foram encontrados agrupamentos",
        "infoEmpty":      "Sem resultados",
        "infoFiltered":   "(filtrado de _MAX_ agrupamentos no total)",
        "lengthMenu":     "Mostrar _MENU_ agrupamentos por página",
        "paginate": {
        "first":      "Início",
        "last":       "Última",
        "next":       "Seguinte",
        "previous":   "Anterior"
        }
        },
        "columnDefs": [ {
      "targets"  : 'no-sort',
      "orderable": false
    }]
} );
} );
