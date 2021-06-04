package cinema.model.cinema;

import cinema.model.enumerations.TypeOfSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**BREVE SPIEGAZIONE CLASSE PHYSICAL SEAT
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * La classe rappresenta il posto fisico che è presente all'interno di una specifica 
 * sala, esistono diversi tipi di posti in un cinema. 
 * Ovviamente si potrebbero aggiungere molti altri attributi a questa classe, ma per semplicità
 * e soprattutto per la realtà che vogliamo rappresentare non ci interessa farlo.
 * Qualora fosse utile per uno specifico cinema aggiungere attributi a questa classe il 
 * programmatore è libero di aggiungerli.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PhysicalSeat {
	
	private TypeOfSeat type;
}
