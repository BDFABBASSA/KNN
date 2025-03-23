package app;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public final class ReadFile  {
                        private double [][] data;
			private String nameFileTrain = new String();
			private String nameFileTest  = new String();
			private FileReader r_train ;
			private FileReader r_test;
			private BufferedReader red_train;
			private BufferedReader red_test;
			private int max_vect ;//= 7922 on chisi  7923 il ya un chifre dans le debut de chaque ligne
                        private float accuracy;
                        private int nbclass  = 0;
                        private Object raf;
      /**
         *constricteur
         **/ 
	public ReadFile(String train,String test,String dectionairPath){
                this.max_vect = getFileSize(dectionairPath)+1;  //dimesion de vecteur
		this.nameFileTrain = train; //nom de fichier d'aprentissage
                this.nameFileTest = test;
                this.data = new double[this.getFileSize(train)][this.max_vect+1];
		try{ 
                    this.r_train   = new FileReader(this.nameFileTrain);
                    this.red_train = new BufferedReader(this.r_train);
                    this.r_test    = new FileReader(this.nameFileTest); 
                    this.red_test  = new BufferedReader(this.r_test);
                }
                catch(IOException e){System.out.println("can not open the file be cause "+e.getMessage());}
        }
       /**
         *fonction qui lancer la classification
         * @return  l'acuuracy
         **/     
	public float clacify(int k){      
		String str_train_ligne;
                String str_test_ligne;
                float[] vect_train; int j; 
                double[] vect_test; 
                double[][] vect_dist ;//= new double[this.getFileSize(this.nameFileTrain)][2];
                float[][] temp;
                float dicide;
                float nbdecisionCorrectes = 0;
                float nbdecisionTotale = this.getFileSize(this.nameFileTest);
                try{ 
                    this.getCourpus();
                    while(true)
                     {    str_test_ligne = red_test.readLine();	
                          if(str_test_ligne == null) break;// boucle infini aret jusqu a parcourir tous le fichier  
                          vect_test = this.transformer(str_test_ligne);                             
                          vect_dist =  distEclidian(vect_test);
                          this.triInsertion(vect_dist);
                          dicide = (float)this.decide(vect_dist,this.nbclass,k);
                          if(vect_test[0]==dicide) //{dicide la dicision de kppv} {vect_test[0] le vari clacc}
                          nbdecisionCorrectes++;                                                        
                     }	
                    this.accuracy = (nbdecisionCorrectes/nbdecisionTotale)*100;
                    inter.str =" Distance utiliser Eclidienne \n Parametre k = "+k;
                    inter.str  =inter.str + "\n nombre de decision Correctes/nombre de decision(" +(int) nbdecisionCorrectes + "/" + (int)nbdecisionTotale + ")";
                }catch(IOException e){System.out.println(e.getMessage());}
                return this.accuracy;
	}
        /**
         *fonction son role cest decopricer train dans matrice
         * @return  un matrice
         **/         
        public void getCourpus()
        {
            String str_train_ligne;
            double vect[];
            double cla=0;
            int i,j=0;
            try {
              while(true)
                {   str_train_ligne = red_train.readLine();
                    if(str_train_ligne == null) break;// boucle infini aret jusqu a parcourir tous le fichier
                    vect = this.transformer(str_train_ligne); 
                    if(j==0)
                    {
                      this.nbclass++;
                      cla =vect[0];                         
                    }
                    if(vect[0]!=cla)
                       {
                        this.nbclass++;
                        cla=vect[0];
                       }
                    
                    for(i=0;i<vect.length;i++)
                    { 
                        this.data[j][i]= vect[i];
                    }
                                            
                    j++;
                }  
            }catch (Exception e){System.out.println(e.getMessage()+"EREUR DE LECTEUR DE FICHIER");}
        }
        /**
         *fonction recoi  String compricee
         *son role cest decopricer se String
         * @param String str_lign
         * @return  un vecteur double
         **/       
        public double[] transformer(String str_lign)
        {
                String[] s2 ;
                double[] vect = new double[this.max_vect];
                String[] temp1;                        
		int i;  
                s2 =  str_lign.split(" ");//s2[0] l`indice s2[1] la valeur
                for (i= 0;i<this.max_vect;i++){ vect[i] = 0;} //prepqrqtion d'un vecteur vide  
                vect[0] = Float.parseFloat(s2[0]);//la class de vecteur 
                for(i = 1;i<s2.length;i++)//remlissage d'un vecteur 
                { 
                   temp1 = s2[i].split(":");
                   vect[Integer.parseInt(temp1[0])]=Float.parseFloat(temp1[1]);
                } 
          return vect;
        }
        /** 
         * fonction recoi  vecteur test
         * calculer le distance et retourner un vecteur de deux dimension
         * la premier dimention c'est la classa qui one calculer la distance avec et 2 cet la distance
         * elle predre duex pramater
         * @param vect2 test
         * @return la distance avec tous les class dans un vecteur
         **/       
        public double[][] distEclidian(double[] vect2)
        {   
            double[][] vect_dist = new double[this.getFileSize(this.nameFileTrain)][2];
            float dist;
            int j=0;
            int i=1;
            int fsize =this.getFileSize(this.nameFileTrain); 
            for( j=0;j<fsize;j++){
                 vect_dist[j][0]=this.data[j][0];//Affictation de la class 
                for(i=1;i<this.max_vect;i++)
                 {                    
                     vect_dist[j][1] += (this.data[j][i]-vect2[i])*(this.data[j][i]-vect2[i]);
                     
                 }
            }
            return vect_dist;//vecteur contient la distace et la class 
        }        
        /** 
         * fonction recoi path de fichier
         * calculer le nbr de ligne de fichier
         * @param strPath de type string  
         * @return le nbr de ligne
         **/ 
        /** 
         * fonction recoi chemin de fichier
         * @param strPath
         * return nombre de ligne
         **/         
        public int getFileSize(String strPath) 
        {    
            String s;
            int i = 0;
            try{ 
                FileReader r  = new FileReader(strPath);
                BufferedReader red= new BufferedReader(r);	
                while(true)
                 {   s = red.readLine();
                     if(s == null) break;
                     i++;
                 }		
            }catch(IOException e){System.out.println(e.getMessage());}
            return i;
        }
        /** 
         * fonction recoi un vecteur et trier ce vecteur on order dicroissan
         * @param tableau   
         **/ 
       public  void triInsertion(double tableau[][])
        {
         double longueur=tableau.length;
         int compt;
        for(int i=1;i<longueur;i++)
            {
             double memory1=tableau[i][1];
             double memory2=tableau[i][0];
             compt=i-1;
             boolean marqueur;
             do{
                marqueur=false;
                if (tableau[compt][1]>memory1)
                    {
                     tableau[compt+1][0]=tableau[compt][0];
                     tableau[compt+1][1]=tableau[compt][1];
                     compt--;
                     marqueur=true;
                    }
                if (compt<0) marqueur=false;
                }
            while(marqueur);
            tableau[compt+1][1]=memory1;
            tableau[compt+1][0]=memory2;
            }
        }
        /** 
         * fonction recoi un vecteur de distance tellque chaque distance a leur class
         * ce fonction fair une decision selon la class majoritairS
         * @param  vect vcteur de distance
         * @param k nombre de distance
         * @return la dicision
         **/       
        public int decide(double[][] vect,int nbC,int k)//float[][] vect,
        {
             int i;
             int[] v = new int[nbC]; //vecteur resultat
             for(i=0;i<nbC;i++) {v[i]=0;}             
             for(i=0;i<k;i++){ v[(int)vect[i][0]-1]++;}           
             int maxcontent = v[0];
             int maxindice = 0;
             for(i =1;i<nbC;i++)
             { 
                 if(v[i]>maxcontent)
                    { maxcontent = v[i];
                      maxindice = i;
                    }                
             }
             return (maxindice+1) ;
        }        
        /** 
         * seter fonction pour acceder aux variabl nameTrain
         * @param  nameTrain
         **/          
        public void setNameTrain(String nameTrain)
        {
            this.nameFileTrain = nameTrain;
        } 
        /** 
         * seter fonction pour acceder aux variabl nameTest
         * @param  nameTest
         **/        
        public void setNameTest(String nameTest)
        {
            this.nameFileTest = nameTest;
        } 
        /** 
         * seter fonction pour acceder aux variabl
         * @param m
         **/        
        public void setMaxVect(int m)
        {
             this.max_vect = m;
        }
        /** 
         * seter fonction pour rotourner nameFileTrain
         * @return nameFileTrain
         **/        
        public String getNameTrain()
        {
           return this.nameFileTrain ;
        } 
        /** 
         * seter fonction pour rotourner nameFileTest 
         * @return nameFileTest
         **/         
        public String getNameTest()
        {
           return this.nameFileTest ;
        } 
        /** 
         * seter fonction pour retourner  max_vect
         * @return max_vect
         **/          
        public int getmaxVect()
        {
            return this.max_vect ;
        } 
}
                       