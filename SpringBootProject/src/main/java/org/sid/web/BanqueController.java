package org.sid.web;


import java.util.Date;
import java.util.List;

import org.sid.entities.Client;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.CompteEpargne;
import org.sid.entities.Operation;
import org.sid.metier.Ibanque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BanqueController {
	@Autowired
	private Ibanque ibanque;
	
	@RequestMapping("/operations")
	public String index(){
		return "comptes";
	}

	@RequestMapping("/consultercompte")   
	public String consulter(Model model,String codeCompte,   
			@RequestParam(name="page", defaultValue="0")int page,
			@RequestParam(name="size", defaultValue="4")int size){
		
		model.addAttribute("codeCompte",codeCompte);
		try {   
			
			Compte cp=ibanque.consulterCopmte(codeCompte);    
			model.addAttribute("compte",cp);       
			
		    Page<Operation> listeOperation = ibanque.listeOperation(codeCompte, page, size);
		    model.addAttribute("listeOperation",listeOperation.getContent());
		    int[] pages = new int[listeOperation.getTotalPages()];
		    model.addAttribute("pages",pages);    
		
		} catch (Exception e) {   
			model.addAttribute("Exception",e);			
		}     
				
		return "comptes";   

	}      
	
	
	@RequestMapping("/saveOperation")
	public String saveOperation(Model model,String typeOperation,String codeCompte,String codeCompte2,double montant){
		try {
			
			if(typeOperation.equals("VER")){
				ibanque.verser(codeCompte, montant);
			}
			else if(typeOperation.equals("RET")){
				ibanque.retirer(codeCompte, montant);
			}else{
				ibanque.virement(codeCompte, codeCompte2, montant);
			}
		} catch (Exception e) {
			model.addAttribute("error",e);
			return "redirect:/consultercompte?codeCompte="+codeCompte+"&error="+e.getMessage();
		}
			    
		
		return "redirect:/consultercompte?codeCompte="+codeCompte;
	}
	
	@RequestMapping("/comptes")
	public String comptes(Model model,	String codeCompte,@RequestParam(name="page", defaultValue="0")int page,
										@RequestParam(name="size", defaultValue="4")int size){
			
		try{
			if(codeCompte!=null){
				
			model.addAttribute("codeCompte",codeCompte);
			Compte cp=ibanque.consulterCopmte(codeCompte);
			model.addAttribute("compte",cp);}
			
			List<Client> listeClient=ibanque.listeClient();
			model.addAttribute("listeClient",listeClient);
			
			Page<Compte> listeCompte= ibanque.listeCompte(page, size);
			
			model.addAttribute("listeCompte",listeCompte.getContent());
			int[] pages = new  int[listeCompte.getTotalPages()];
			model.addAttribute("pages",pages);
		}catch (Exception e) {
			model.addAttribute("Exception",e);
			
		}
		
		return "compte";
	}
	
	@RequestMapping("/addCompte")
	public String addCompte(Model model ,String typecompte,String codeCompte,
			double solde,Double decouvert,Double taux,Long client){
		Client c=ibanque.chercherClient(client);
		try{
			if(typecompte.equals("CompteCourant")){
				ibanque.addCompte(new CompteCourant(codeCompte, new Date(), solde, c, decouvert));
						
			}
			if(typecompte.equals("CompteEpargne")){
				ibanque.addCompte(new CompteEpargne(codeCompte, new Date(), solde, c, taux));
							
			}
			
		}catch (Exception e) {
			model.addAttribute("Ex",e);
			System.out.println(e);
		}
		
		
		return "redirect:/comptes";
	
	}
	
	@RequestMapping("/deleteCompte")
	public String deleteCompte(Model model ,String codeCompte){
	
		try{
		ibanque.deletecompte(codeCompte);
			
		}catch (Exception e) {
			model.addAttribute("Ex",e);
			
		}
		
		
		return "redirect:/comptes";
	
	}
	
	
	@RequestMapping("/client")
	public String client(Model model,Long code,String nom,String email,	@RequestParam(name="page", defaultValue="0")int page,
										@RequestParam(name="size", defaultValue="4")int size){
		
		try{
			Page<Client> listeClient= ibanque.listeClient(page, size);
			model.addAttribute("listeclient",listeClient.getContent());
			model.addAttribute("nom",nom);
			model.addAttribute("code",code);
			model.addAttribute("email",email);
			int[] pages = new  int[listeClient.getTotalPages()];
			model.addAttribute("pages",pages);
		}catch (Exception e) {
			model.addAttribute("Exception",e);
			
		}
		
		return "clients";
	}
	@RequestMapping("/saveclient")
	public String saveclient(Model model,Long code,String nom,String email){
		
		try{
			Client c= new Client();
			c.setCode(code);
			c.setEmail(email);
			c.setNom(nom);
			ibanque.saveClient(c);
		}catch (Exception e) {
			model.addAttribute("Exception",e);
			
		}
		
		return "redirect:/client";
	}
	
	@RequestMapping("/deleteclient")
	public String deleteClient(Model model,Long code){
		
		try{
			ibanque.deleteClient(code);
		}catch (Exception e) {
			model.addAttribute("Exception",e);
			
		}
		
		return "redirect:/client";
	}

}
