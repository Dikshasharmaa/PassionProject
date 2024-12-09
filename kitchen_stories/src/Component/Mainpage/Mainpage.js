import React from 'react'
import './Mainpage.css';


const Mainpage = () => {
  return (
    <>
    <div className = 'container' >
        <div className='searchBar'>
            <input type='text' placeholder='What do yo want to eat today!'/>
            <button>Search</button>
        </div>
    </div>
    </>
    
  )
}

export default Mainpage