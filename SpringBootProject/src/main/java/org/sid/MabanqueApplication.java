package org.sid;

import java.util.Date;

import org.sid.dao.ClientRepository;
import org.sid.dao.CompteRepository;
import org.sid.dao.OperationRepository;
import org.sid.entities.Client;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.CompteEpargne;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MabanqueApplication{
    
	@Autowired
    private ClientRepository clientRepository ;   
	@Autowired
	private CompteRepository compteRepository ;  
	
	@Autowired
	private OperationRepository oppR ; 
	
	public static void main(String[] args) {
		SpringApplication.run(MabanqueApplication.class, args);
		
	}

	public void run(String... arg0) throws Exception { 
		Client c1 = clientRepository.save(new Client("toto","a@a")); 
		Client c2 = clientRepository.save(new Client("momo","a@a"));  
	    
		Compte cpt1 = 
				compteRepository.
				save(new CompteEpargne("az",new Date(),65300,c1, 6000)) ; 
		Compte cpt2 = 
				compteRepository.
				save(new CompteCourant("ak",new Date(),1280,c2, 10.5)) ;  
		
		Operation ope1 = 
				oppR.save(new Versement(new Date(),9999,cpt1));  
		Operation ope2 = 
				oppR.save(new Versement(new Date(),432,cpt1)); 
		Operation ope3 = 
				oppR.save(new Retrait(new Date(),123456,cpt1));  
		Operation ope4 = 
				oppR.save(new Retrait(new Date(),98765,cpt1));
	
	} 
	
}
