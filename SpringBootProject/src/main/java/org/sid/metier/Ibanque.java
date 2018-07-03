package org.sid.metier;

import java.util.List;

import org.sid.entities.Client;
import org.sid.entities.Compte;
import org.sid.entities.Operation;
import org.springframework.data.domain.Page;

public interface Ibanque {
	
	public Compte consulterCopmte(String codeCompte);
	public void verser(String codeCompte, double montant);
	public void retirer(String codeCompte, double montant);
	public void virement(String codeCompte1,String codeCompte2, double montant);
	public Page<Operation> listeOperation(String codeCompte,int page, int size);
	public Page<Compte> listeCompte(int page, int size);
	public Page<Client> listeClient(int page, int size);
	public   List<Client> listeClient();
	public Client chercherClient(Long client);
	public Client saveClient(Client client);
	public void deleteClient(Long client);
	public Compte addCompte( Compte compte);
	public void deletecompte(String compte);

}
