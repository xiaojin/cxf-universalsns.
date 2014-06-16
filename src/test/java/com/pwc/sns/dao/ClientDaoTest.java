package com.pwc.sns.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.pwc.sns.dto.Client;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class ClientDaoTest {

    @Autowired 
    ClientDao clientDao;

    @Test
    public void findOneTest() {
        Client client = clientDao.findClientByUdid("not-exist");
        assertNull(client);
        assertTrue(client == null);
    }
    
    @Test
    public void add(){
    	Client client = new Client();
    	client.setName("test");
    	client.setStatus(1);
    	client.setClientType("1");
    	clientDao.addClient(client);
    	assert(clientDao.len() > 0);
    	
    }
}