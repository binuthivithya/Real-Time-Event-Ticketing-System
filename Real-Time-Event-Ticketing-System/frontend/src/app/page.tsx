"use client"
import axios from "axios"
import { useState, useEffect } from "react"

import { LineChart, Line, XAxis, YAxis, CartesianGrid, ResponsiveContainer } from "recharts"
import { Minus, Plus, Play, Square } from 'lucide-react'

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Progress } from "@/components/ui/progress"
import { ChartContainer, ChartConfig } from "@/components/ui/chart"

// Sample data for the line chart
const data = [
  { time: "0s", tickets: 0 },
  { time: "10s", tickets: 20 },
  { time: "20s", tickets: 35 },
  { time: "30s", tickets: 50 },
  { time: "40s", tickets: 70 },
  { time: "50s", tickets: 85 },
  { time: "60s", tickets: 100 },
]

export const startSystem = async (vendors: number, customers: number, vips: number) => {
  try {
    const response = await axios.post<boolean>(http://localhost:8080/ticketing/start/${vendors}/${customers}/${vips});
    return response.data;
  } catch (error) {
    console.error("Error starting the system:", error);
    alert("Failed to start the system.");
    return false;
  }
};

export const stopSystem = async () => {
  try {
    const response = await axios.post<boolean>("http://localhost:8080/ticketing/stop");
    return response.data;
  } catch (error) {
    console.error("Error stopping the system:", error);
    alert("Failed to stop the system.");
    return false;
  }
};

export default function TicketingDashboard() {
  const [isActive, setIsActive] = useState(false)
  const [vendors, setVendors] = useState(1)
  const [customers, setCustomers] = useState(1)
  const [vips, setVips] = useState(0)
  const [totalTickets, setTotalTickets] = useState(100)
  const [maxCapacity, setMaxCapacity] = useState(200)
  const [releaseRate, setReleaseRate] = useState(5)
  const [retrievalRate, setRetrievalRate] = useState(3)
  const [progress, setProgress] = useState(() => Math.round((35 / 100) * 100))

  useEffect(() => {
    setProgress(Math.round((35 / totalTickets) * 100))
  }, [totalTickets])

  const handleSave = () => {
    // Handle saving configuration
    console.log("Saving configuration...")
  }

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
            onClick={() => setIsActive(!isActive)}
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
                onClick={() => setVendors(Math.max(1, vendors - 1))}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="text-2xl font-bold">{vendors}</span>
              <Button
                variant="outline"
                size="icon"
                onClick={() => setVendors(vendors + 1)}
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
                onClick={() => setCustomers(Math.max(1, customers - 1))}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="text-2xl font-bold">{customers}</span>
              <Button
                variant="outline"
                size="icon"
                onClick={() => setCustomers(customers + 1)}
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
                onClick={() => setVips(Math.max(0, vips - 1))}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="text-2xl font-bold">{vips}</span>
              <Button
                variant="outline"
                size="icon"
                onClick={() => setVips(vips + 1)}
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
                value={maxCapacity}
                onChange={(e) => setMaxCapacity(Number(e.target.value))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="release-rate">Ticket Release Rate (s)</Label>
              <Input
                id="release-rate"
                type="number"
                min="1"
                value={releaseRate}
                onChange={(e) => setReleaseRate(Number(e.target.value))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="retrieval-rate">Customer Retrieval Rate (s)</Label>
              <Input
                id="retrieval-rate"
                type="number"
                min="1"
                value={retrievalRate}
                onChange={(e) => setRetrievalRate(Number(e.target.value))}
              />
            </div>
          </div>
          <Button onClick={handleSave} className="w-full">Save Configuration</Button>
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
              <span>{progress}%</span>
            </div>
            <Progress value={progress} className="h-2" />
            <div className="text-center text-sm text-muted-foreground">
              {Math.round(totalTickets * (progress / 100))} / {totalTickets} tickets
            </div>
          </div>
          <div className="h-[200px] mt-4">
            <ChartContainer
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
            </ChartContainer>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

