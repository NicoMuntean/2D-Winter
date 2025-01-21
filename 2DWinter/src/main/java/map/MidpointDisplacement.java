package map;

import java.util.Arrays;

public class MidpointDisplacement {
    
    private int[][] map;
    
    public MidpointDisplacement(int h, int w,int nOrders,int[] heightConstraints) {
        
        map = new int[w][h];
        fillMap(map,nOrders,h-1,w-1,heightConstraints);
        
    }
    
    private int[] calcAlterpoints(int w,int order) {
        
        int[] points = new int[(int)(Math.pow(2,order-1))];
        int addCnt = 0;
        
        for(int i = 0; i < w; i++) {
            
            if(i%(w/Math.pow(2,order)) == 0 && i%(w/Math.pow(2, order-1))!=0) {
                points[addCnt] = i;
                addCnt++;
            }
        }
        
        return points;
    }
    
    private int[][] fillMap(int[][] workArr,int nOrder,int h, int w, int[] hC) {
        
        int[] initHVals = {(int)(Math.random()*(hC[1]-hC[0])/2 + hC[0]),(int)(Math.random()*(hC[1]-hC[0])/2 + hC[0])};
        
        workArr[0][initHVals[0]] = 2;
        workArr[w][initHVals[1]] = 2;
        
        for(int i = 0; i < nOrder; i++) {
            
            int stepsize = 0;
            if(i == 0) 
                stepsize = w/2;
            else
                stepsize = (int)(w/(Math.pow(2,i)));
            
            int[] alterpoints = calcAlterpoints(w,i);
            
            for(int j : alterpoints) {
                
                int hVal1 = 0;
                int hVal2 = 0;
                
                for(int k = 0; k < h; k++) {
                    
                    if(workArr[j-stepsize][k]==2)
                        hVal1 = k;
                    if(workArr[j+stepsize][k]==2)
                        hVal2 = k;
                }
                
                System.out.println((hVal1+hVal2)/2);
                int chosenPoint = (int)(Math.random()*(hVal1+hVal2)/(Math.pow(2,i)) + (hVal1+hVal2)/2 - (hVal1+hVal2)/(Math.pow(2,i+1)));
                workArr[j][chosenPoint] = 2;
                
                if(i==nOrder-1){
                    for(int k = -stepsize;k < 0;k++) {
                        
                        int avgPoint = hVal1 + (int)((chosenPoint-hVal1) * (((float)stepsize+(float)k)/(float)stepsize));
                        workArr[j+k][avgPoint] = 2;
                        int cnt = 0;
                        for(int l = avgPoint+1; l < h; l++) {
                            if(cnt < 5) {
                                workArr[j+k][l] = 3;
                            }
                            else
                                workArr[j+k][l] = 1;
                            cnt++;
                        }
                        
                    }
                    for(int k = 0;k < stepsize;k++) {
                        
                        int avgPoint = chosenPoint + (int)(((float)hVal2-(float)chosenPoint) * ((float)k/(float)stepsize));
                        workArr[j+k][avgPoint] = 2;
                        int cnt = 0;
                        for(int l = avgPoint+1; l < h; l++) {
                            if(cnt < 5) {
                                workArr[j+k][l] = 3;
                            }
                            else
                                workArr[j+k][l] = 1;
                            cnt++;
                        }
                    }
                }
                
            }
            
            
            
        }
        return workArr;
    }
    
    public int[][] getMap() {
        return map;
    } 
}
