/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery.fn.dataTable.render.ellipsis = function ( cutoff, wordbreak, escapeHtml ) {
    var esc = function ( t ) {
        return t
            .replace( /&/g, '&amp;' )
            .replace( /</g, '&lt;' )
            .replace( />/g, '&gt;' )
            .replace( /"/g, '&quot;' );
    };
 
    return function ( d, type, row ) {
        // Order, search and type get the original data
        if ( type !== 'display' ) {
            return d;
        }
 
        if ( typeof d !== 'number' && typeof d !== 'string' ) {
            return d;
        }
 
        d = d.toString(); // cast numbers
 
        if ( d.length <= cutoff ) {
            return d;
        }
 
        var shortened = d.substr(0, cutoff-1);
 
        // Find the last white space character in the string
        if ( wordbreak ) {
            shortened = shortened.replace(/\s([^\s]*)$/, '');
        }
 
        // Protect against uncontrolled HTML input
        if ( escapeHtml ) {
            shortened = esc( shortened );
        }
 
        return '<span class="ellipsis" title="'+esc(d)+'">'+shortened+'&#8230;</span>';
    };
};


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
    $('#example thead tr:eq(0) th:eq(0)').html("");
 
    var table = $('#example').DataTable( {
        orderCellsTop: true,
        fixedHeader: true,
        order: [],
        "language": {
        "search": "Procurar: ",
        "emptyTable":     "A tabela de padrões está vazia",
        "info": "A mostrar entre _START_ a _END_ no total de _TOTAL_ padrões",
        "zeroRecords":    "Não foram encontrados padrões",
        "infoEmpty":      "Sem resultados",
        "infoFiltered":   "(filtrado de _MAX_ padrões no total)",
        "lengthMenu":     "Mostrar _MENU_ padrões por página",
        "paginate": {
        "first":      "Início",
        "last":       "Última",
        "next":       "Seguinte",
        "previous":   "Anterior"
        }
        },
         "columnDefs": [ 
            {targets: 2,render: $.fn.dataTable.render.ellipsis( 30, true )}, 
            {targets: 'no-sort',orderable: false}
        ]
} );
} );
