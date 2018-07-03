package org.sid.metier;

import java.util.Date;
import java.util.List;

import org.sid.dao.ClientRepository;
import org.sid.dao.CompteRepository;
import org.sid.dao.OperationRepository;
import org.sid.entities.Client;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class BanqueMetierImpl implements Ibanque {
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	 
	public Compte consulterCopmte(String codeCompte) {
		Compte cp=compteRepository.findOne(codeCompte);
		if(cp==null)throw new RuntimeException("le compte introuvable");
		return cp;
	}

	 
	public void verser(String codeCompte, double montant) {
		
		Compte cp=consulterCopmte(codeCompte);
		
		Versement v= new Versement(new Date(), montant, cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
		
	}

	 
	public void retirer(String codeCompte, double montant) {
		Compte cp=consulterCopmte(codeCompte);
		double fc=0;
		if(cp instanceof CompteCourant)
			fc= ((CompteCourant) cp).getDecouvert();
		if(cp.getSolde()+fc<montant)
			 throw new RuntimeException("solde insufisant");
			
		Retrait r= new Retrait(new Date(), montant, cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()-montant);
		compteRepository.save(cp);
	}

	public void virement(String codeCompte1, String codeCompte2, double montant) {
		if(codeCompte1.equals(codeCompte2))  throw new RuntimeException("imposible virement dans le meme compte");
		retirer(codeCompte1, montant);
		verser(codeCompte2, montant);
	}

	
	public Page<Operation> listeOperation(String codeCompte, int page, int size) {
		
		return operationRepository.listOperation(codeCompte, new PageRequest(page, size));
	}

	
	public Page<Compte> listeCompte(int page, int size) {
		return  compteRepository.findAll(new PageRequest(page, size));
	
	}

	
	public Page<Client> listeClient(int page, int size) {
		
		return  clientRepository.findAll(new PageRequest(page, size));
		
	}

	 
	public List<Client> listeClient() {
		
		return clientRepository.findAll();
	}

	 
	public Compte addCompte(Compte compte) {
		return compteRepository.saveAndFlush(compte);
		
	}

	 
	public Client chercherClient(Long client) {
		
		return clientRepository.findOne(client);
	}

	 
	public Client saveClient(Client client) {
		return clientRepository.saveAndFlush(client);
	}

	 
	public void deleteClient(Long client) {
		
		 clientRepository.delete(client);
	}

	 
	public void deletecompte(String compte) {
		compteRepository.delete(compte);
		
	}

}
