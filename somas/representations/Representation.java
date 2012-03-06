/*
 * Copyright 2007-2009 Eric Holloway.  All rights reserved.
 */
    package representations;

import java.io.Serializable;

import representationUtil.Coder;

public abstract class Representation implements Serializable {
    protected int totalsize = 0;
    protected boolean[][] chromosome;
    protected Coder coder = null;
        
    public abstract void encode(Object[] vals);
    public abstract Object[] decode();
        
    public Representation() {super();} // TODO make it so this can only be initialized on certain superclasses, i.e. not on IntBitRep
    
    public Representation(Object[] vals, Coder c) {
        coder = c;
                
        encode(vals);
    }
        
    public Representation(Representation r) {
        totalsize = r.totalsize;
        coder = r.coder;
        chromosome = r.getChromosomeCopy();
    }
        
    // MESSY inconsistent access methods - chromosome accessed directly, other fields through encapsulation
    public Representation(Representation r, Coder c) {
        this(r);
                
        if(coder.getGeneSize() != c.getGeneSize())
            if(c.getGeneSize() != 0) {
                if(coder.getGeneSize() != 0) {
                    chromosome = new boolean[r.chromosome.length][coder.getGeneSize()];
                                        
                    for(int i = 0; i < r.chromosome.length; i++)
                        chromosome[i] = c.adjust(r.chromosome[i]);
                } 
                else { // r's chromosome doesn't know its genes
                    assert(chromosome.length % c.getGeneSize() == 0);
                    boolean[][] newChromosome = new boolean[r.chromosome.length / c.getGeneSize()][c.getGeneSize()];
                                        
                    int necessaryLength = chromosome.length - (chromosome.length % c.getGeneSize());
                                        
                    for(int i = 0; i < necessaryLength; i++)
                        newChromosome[i/c.getGeneSize()][i%c.getGeneSize()] = chromosome[i][0];
                                        
                    chromosome = newChromosome;
                }
            }
            else { // this chromosome doesn't know its genes
                boolean[][] newChromosome = new boolean[r.getTotalSize()][1];

                for(int i = 0, k = 0; i < r.chromosome.length; i++)
                    for(int j = 0; j < r.chromosome[i].length; j++, k++)
                        newChromosome[k][0] = r.chromosome[i][j];
                                
                chromosome = newChromosome;
            }
        // On the other hand, if both representations have known genes, and the gene sizes match, then don't do any conversion 
                
        coder = c;
    }
        
    // Uniform crossover
    public boolean[][] crossover(Representation r) {
        assert(r.getClass() == this.getClass());
                
        boolean[][] chromo2 = r.getChromosomeCopy();
        boolean[][] newgenes = new boolean[chromosome.length][];

        for(int i = 0; i < chromosome.length; i++) {
            newgenes[i] = new boolean[chromosome[i].length];
                        
            for(int j = 0; j < chromosome[i].length; j++)
                newgenes[i][j] = Math.random() < .5 ? chromosome[i][j]:chromo2[i][j]; 
        }
                
        return newgenes;
    }
        
    public boolean[][] mutate() {
        boolean[][] newgenes = getChromosomeCopy();
                
        int size = coder.getGeneSize();
        if(size == 0) size = 1;
                
        int k =(int)(Math.random() * totalsize);
                
        newgenes[k/size][k%size] = !newgenes[k/size][k%size]; 

        return newgenes;
    }
        
    public boolean[][] getChromosomeCopy() { return chromosome.clone(); }
    public void setChromosome(boolean[][] new_c) { chromosome = new_c.clone(); }
    public int getTotalSize() { return totalsize; }
    public void setTotalSize(int t) { totalsize = t; }
}
