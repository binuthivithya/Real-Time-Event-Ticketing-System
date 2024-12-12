"use client"
import axios from "axios"
import { useState, useEffect } from "react"

import { Minus, Plus, Play, Square } from 'lucide-react'

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Progress } from "@/components/ui/progress"

export const startSystem = async (vendors: number, customers: number, vips: number) => {
  try {
    const response = await axios.post<boolean>(`http://localhost:8080/system/start/${vendors}/${customers}/${vips}`);
    return response.data;
  } catch (error) {
    console.error("Error starting the system:", error);
    alert("Failed to start the system.");
    return false;
  }
};

export const stopSystem = async () => {
  try {
    const response = await axios.post<boolean>("http://localhost:8080/system/stop");
    
    return response.data;
  } catch (error) {
    console.error("Error stopping the system:", error);
    alert("Failed to stop the system.");
    return false;
  }
};






export default function TicketingDashboard() {
  const [isActive, setIsActive] = useState(false);
  const [vendors, setVendors] = useState(1);
  const [customers, setCustomers] = useState(1);
  const [vips, setVips] = useState(0);

  const [totalTickets, setTotalTickets] = useState<number>(Number);
  const [maxTicketCapacity, setMaxCapacity] = useState<number>(Number);
  const [ticketReleasedRate, setReleaseRate] = useState<number>(Number);
  const [customerRetrievalRate, setRetrievalRate] = useState<number>(Number);

  const [ticketPool, setTicketPool] = useState<number>(Number);

  const [error, setError] = useState<string | null>(null);


  useEffect(() => {
    // Websocket connection to load configuration settings
    const loadConfigurations = async () => {
      try {
        const configSettings = await fetch("Http://localhost:8080/configuration/load");
        if (!configSettings.ok) {
          throw new Error("Failed to load configuration.");
        }
        const config = await configSettings.json();

        setTotalTickets(config.totalTickets);
        setMaxCapacity(config.maxTicketCapacity);
        setReleaseRate(config.ticketReleasedRate);
        setRetrievalRate(config.customerRetrievalRate);
      }catch (error) {
        console.error("Error loading configuration:", error);
        alert("Failed to load configuration.");
      }
    };

    loadConfigurations();

    //Websocket connection to get the number of tickets in the ticket pool.
    const ticketPoolWebSocket = new WebSocket("ws://localhost:8080/ws/ticketpool");
    ticketPoolWebSocket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        setTicketPool(data);
      } catch (error) {
        console.error("Error parsing ticket pool data:", error);
      }
    }

    //Websocket connection to get the system status.
    const systemStateWebSocket = new WebSocket("ws://localhost:8080/ws/systemstatus");
    systemStateWebSocket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        setIsActive(data);
      } catch (error) {
        console.error("Error parsing system status data:", error);
      }
    }

  
  }, [])

  const toggleSystemState = async () => {
    if (isActive) {
      const success = await stopSystem();
      if (success) {
        setIsActive(false);
        alert("System stopped successfully.");
      }
    } else {
      const success = await startSystem(vendors, customers, vips);
      if (success) {
        setIsActive(true);
        alert("System started successfully.");
      }
    }
  };

  const handleSave = async () => {
    if(
      totalTickets <= 0 || maxTicketCapacity <= 0 || ticketReleasedRate <= 0 || customerRetrievalRate <= 0
    ) {

      setError("Please enter a positive number for all fields.");
      return;
    }
    setError(null);

    const config = {
      totalTickets,
      maxTicketCapacity,
      ticketReleasedRate,
      customerRetrievalRate,
    };

    try {
      const response = await axios.post("http://localhost:8080/configuration/save", config);

      alert("Configuration saved successfully.");
      console.log("Configuration saved successfully.", response.data);
    } catch (error) {
      console.error("Error saving configuration:", error);
      alert("Failed to save configuration.");
    }
    console.log("Saving configuration...")
  }

  const increaseVendors = async () => {
    try {
      await fetch(`http://localhost:8080/system/addVendor?vendorName=Vendor ${vendors + 1}`,{method: "POST"});
      setVendors(vendors + 1);
    } catch (error) {
      console.error("Error increasing vendors:", error);
      alert("Failed to increase vendors.");
    }
  };

  const decreaseVendors = async () => {
    if (vendors > 1) {
      try {
        await fetch(`http://localhost:8080/system/removeVendor?vendorName=Vendor ${vendors}`,{method: "DELETE"});
        setVendors((prev) => prev - 1)
      } catch (error) {
        console.error("Error decreasing vendors:", error);
        alert("Failed to decrease vendors.");
        return null;
      }
    }
  };

  

const increaseCustomers = async () => {
  try {
    await fetch(`http://localhost:8080/system/addCustomer?customerName=Customer ${customers + 1}`,{method: "POST"});
    setCustomers(customers + 1);
  } catch (error) {
    console.error("Error increasing customers:", error);
    alert("Failed to increase customers.");
    return null;
  }
};

const decreaseCustomers = async () => {
  if (customers > 1) {
    try {
      await fetch(`http://localhost:8080/system/removeCustomer?customerName=Customer ${customers}`,{method: "DELETE"});
      setCustomers((prev) => prev - 1)
    } catch (error) {
      console.error("Error decreasing customers:", error);
      alert("Failed to decrease customers.");
      return null;
    }
  }
};

const increaseVips = async () => {
  try {
    await fetch(`http://localhost:8080/system/addVip?vipName=Vip ${vips + 1}`,{method: "POST"});
    setVips(vips + 1);
  } catch (error) {
    console.error("Error increasing Vips Customer:", error);
    alert("Failed to increase Vip Customer.");
    return null;
  }
};

const decreaseVips = async () => {
  if (vips > 0) {
    try {
      await fetch(`http://localhost:8080/system/removeVip?vipName=Vip ${vips}`,{method: "DELETE"});
      setVips((prev) => prev - 1)
    } catch (error) {
      console.error("Error decreasing VIP customers:", error);
      alert("Failed to decrease VIP customers.");
      return null;
    }
  }
};

  return (
    <div className="p-6 space-y-6 max-w-6xl mx-auto">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Ticketing Dashboard</h1>
        <div className="flex items-center gap-4">
          <span className={`font-medium ${isActive ? "text-green-500" : "text-red-500"}`}>
            System: {isActive ? "Active" : "Inactive"}
          </span>
          <Button
            variant={isActive ? "destructive" : "default"}
            size="icon"
            onClick={toggleSystemState}
          >
            {isActive ? <Square className="h-4 w-4" /> : <Play className="h-4 w-4" />}
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardHeader>
            <CardTitle>Vendors</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <Button
                variant="outline"
                size="icon"
                onClick={decreaseVendors}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="text-2xl font-bold">{vendors}</span>
              <Button
                variant="outline"
                size="icon"
                onClick={increaseVendors}
                disabled={!isActive}
              >
                <Plus className="h-4 w-4" />
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Customers</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <Button
                variant="outline"
                size="icon"
                onClick={decreaseCustomers}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="text-2xl font-bold">{customers}</span>
              <Button
                variant="outline"
                size="icon"
                onClick={increaseCustomers}
                disabled={!isActive}
              >
                <Plus className="h-4 w-4" />
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>VIPs</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <Button
                variant="outline"
                size="icon"
                onClick={decreaseVips}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="text-2xl font-bold">{vips}</span>
              <Button
                variant="outline"
                size="icon"
                onClick={increaseVips}
                disabled={!isActive}
              >
                <Plus className="h-4 w-4" />
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Configuration</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="total-tickets">Total Tickets</Label>
              <Input
                id="total-tickets"
                type="number"
                min="1"
                value={totalTickets}
                onChange={(e) => setTotalTickets(Number(e.target.value))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="max-capacity">Maximum Ticket Capacity</Label>
              <Input
                id="max-capacity"
                type="number"
                min="1"
                value={maxTicketCapacity}
                onChange={(e) => setMaxCapacity(Number(e.target.value))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="release-rate">Ticket Release Rate (s)</Label>
              <Input
                id="release-rate"
                type="number"
                min="1"
                value={ticketReleasedRate}
                onChange={(e) => setReleaseRate(Number(e.target.value))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="retrieval-rate">Customer Retrieval Rate (s)</Label>
              <Input
                id="retrieval-rate"
                type="number"
                min="1"
                value={customerRetrievalRate}
                onChange={(e) => setRetrievalRate(Number(e.target.value))}
              />
            </div>
          </div>
          <Button onClick={handleSave} className="w-full" disabled={isActive}>Save Configuration</Button>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Ticket Activity</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <div className="flex justify-between text-sm text-muted-foreground">
              <span>Ticket Pool</span>
              <span>100%</span>
            </div>
            <Progress value={(ticketPool/maxTicketCapacity)*100} className="h-2" />
            <div className="text-center text-sm text-muted-foreground">
              {ticketPool} / {maxTicketCapacity} tickets
            </div>
          </div>
          <div className="h-[200px] mt-4">
            {/* <ChartContainer
              config={{
                tickets: {
                  label: "Tickets",
                  color: "hsl(var(--primary))",
                },
              }}
            >
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={data}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="time" />
                  <YAxis />
                  <Line
                    type="monotone"
                    dataKey="tickets"
                    stroke="var(--color-tickets)"
                    strokeWidth={2}
                  />
                </LineChart>
              </ResponsiveContainer>
            </ChartContainer> */}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

